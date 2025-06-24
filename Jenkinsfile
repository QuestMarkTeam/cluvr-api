pipeline {
    agent any

    environment {
        AWS_REGION = 'us-west-2'
        AWS_ACCOUNT_ID = '617373894870'
        ECR_REPO = 'cluvr-api'
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGE_TAG = 'latest'
        EC2_IP = '44.239.99.137'
        ENV_PATH = '/home/ubuntu/.env'
    }

    stages {
        stage('Create .env & Send to EC2') {
            steps {
                echo '✅ Generating .env and sending to EC2...'
                withCredentials([
                    string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                    string(credentialsId: 'DB_HOST', variable: 'DB_HOST'),
                    string(credentialsId: 'DB_PORT', variable: 'DB_PORT'),
                    string(credentialsId: 'DB_NAME', variable: 'DB_NAME'),
                    string(credentialsId: 'DB_USERNAME', variable: 'DB_USERNAME'),
                    string(credentialsId: 'DB_PASSWORD', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'REDIS_HOST', variable: 'REDIS_HOST'),
                    string(credentialsId: 'REDIS_PORT', variable: 'REDIS_PORT'),
                    string(credentialsId: 'RMQ_HOST', variable: 'RMQ_HOST'),
                    string(credentialsId: 'RMQ_PORT', variable: 'RMQ_PORT'),
                    string(credentialsId: 'RMQ_USERNAME', variable: 'RMQ_USERNAME'),
                    string(credentialsId: 'RMQ_PASSWORD', variable: 'RMQ_PASSWORD'),
                ]) {
                    sh """
                        echo "JWT_SECRET_KEY=${JWT_SECRET_KEY}" > .env
                        echo "SPRING_DATASOURCE_URL=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8" >> .env
                        echo "SPRING_DATASOURCE_USERNAME=${DB_USERNAME}" >> .env
                        echo "SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}" >> .env
                        echo "REDIS_HOST=${REDIS_HOST}" >> .env
                        echo "REDIS_PORT=${REDIS_PORT}" >> .env
                        echo "RMQ_HOST=${RMQ_HOST}" >> .env
                        echo "RMQ_PORT=${RMQ_PORT}" >> .env
                        echo "RMQ_USERNAME=${RMQ_USERNAME}" >> .env
                        echo "RMQ_PASSWORD=${RMQ_PASSWORD}" >> .env

                        scp -o StrictHostKeyChecking=no -i /var/lib/jenkins/.ssh/id_rsa .env ubuntu@${EC2_IP}:${ENV_PATH}
                    """
                }
            }
        }

        stage('Checkout SCM') {
            steps {
                cleanWs() // 작업 공간 비우기
                echo "✅ Checking out source code from GitHub..."
                checkout scm
            }
        }

        stage('Build & Deploy only if on develop branch') {
            when {
                allOf {
                    branch 'develop' // develop 브랜치일 때만
                }
            }

            steps {
                echo "✅ Deploying develop branch build..."

                // Build the Docker image
                script {
                    sh '''
                    docker build -t $ECR_REPO:$IMAGE_TAG .
                    docker tag $ECR_REPO:$IMAGE_TAG $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    '''
                }

                // AWS ECR Login and Push Image
                script {
                    sh '''
                    aws ecr get-login-password --region $AWS_REGION \
                        | docker login --username AWS --password-stdin $ECR_REGISTRY

                    docker push $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    '''
                }

                // SCP and SSH to EC2 to deploy
                script {
                    // 1. 호스트 키를 자동으로 등록
                    sh '''
                    ssh-keyscan -H $EC2_IP >> ~/.ssh/known_hosts
                    '''

                    // 1. 네트워크가 없으면 생성 (이미 있으면 무시)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "docker network create cluvr-net 2>/dev/null || echo 'Network already exists'"
                    '''

                    // 2. 의존성 서비스들이 실행 중인지 확인하고 없으면 시작 (이미 실행중이면 스킵)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP '
                        echo "🔍 의존성 서비스들 상태 확인 중..."

                        # RabbitMQ 체크 및 시작
                        if [ -z "$(docker ps -q -f name=rabbitmq)" ]; then
                            echo "📦 RabbitMQ 시작 중..."
                            docker run -d --name rabbitmq --network cluvr-net -p 5672:5672 -p 15672:15672 --restart unless-stopped \
                                -e RABBITMQ_DEFAULT_USER=${RMQ_USERNAME} \
                                -e RABBITMQ_DEFAULT_PASS=${RMQ_PASSWORD} \
                                rabbitmq:3-management
                        else
                            echo "✅ RabbitMQ 이미 실행 중 - 스킵"
                        fi

                        # Redis 체크 및 시작
                        if [ -z "$(docker ps -q -f name=redis)" ]; then
                            echo "📦 Redis 시작 중..."
                            docker run -d --name redis --network cluvr-net -p 6379:6379 --restart unless-stopped redis:7.2
                        else
                            echo "✅ Redis 이미 실행 중 - 스킵"
                        fi

                        echo "✅ 의존성 서비스들 준비 완료!"
                    '
                    '''

                    // 3. cluvr-api 앱만 재시작 (매번 새로 배포)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🔄 기존 cluvr-api 앱 중지 중...'
                        docker stop cluvr-api 2>/dev/null || true
                        docker rm cluvr-api 2>/dev/null || true
                        echo '✅ 기존 앱 정리 완료'
                    "
                    '''

                    // 4. ECR에서 최신 이미지 가져오기
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🔐 ECR 로그인 중...'
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                        echo '📥 최신 이미지 다운로드 중...'
                        docker pull $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    "
                    '''

                    // 5. 새로운 cluvr-api 앱 시작
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🚀 새로운 cluvr-api 앱 시작 중...'
                        docker run -d --name cluvr-api --network host \
                                                    --env-file ${ENV_PATH} \
                                                    --log-driver json-file \
                                                    --log-opt max-size=10m \
                                                    --log-opt max-file=3 \
                                                    --restart unless-stopped \
                                                    $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                        echo '🎉 배포 완료! 앱이 실행 중입니다.'
                        echo '📍 접속 주소: http://44.239.99.137'
                    "
                    '''
                }
            }
        }
    }
}
