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
                    string(credentialsId: 'app_password', variable: 'app_password'),
                    string(credentialsId: 'ACCESS_AWS', variable: 'ACCESS_AWS'),
                    string(credentialsId: 'CLIENT_ID', variable: 'CLIENT_ID'),
                    string(credentialsId: 'CLIENT_SECRET', variable: 'CLIENT_SECRET'),
                    string(credentialsId: 'SECRET_AWS', variable: 'SECRET_AWS'),
                    string(credentialsId: 'USER_POOL_ID', variable: 'USER_POOL_ID'),
                    string(credentialsId: 'COGNITO_REDIRECT_URI', variable: 'COGNITO_REDIRECT_URI'),
                    string(credentialsId: 'COGNITO_TOKEN_ENDPOINT', variable: 'COGNITO_TOKEN_ENDPOINT'),
                    string(credentialsId: 'COGNITO_JWK_SET_URI', variable: 'COGNITO_JWK_SET_URI'),
                    string(credentialsId: 'SPRING_MAIL_HOST', variable: 'SPRING_MAIL_HOST'),
                    string(credentialsId: 'SPRING_MAIL_PORT', variable: 'SPRING_MAIL_PORT'),
                    string(credentialsId: 'SPRING_MAIL_USERNAME', variable: 'SPRING_MAIL_USERNAME'),
                    string(credentialsId: 'SPRING_MAIL_PASSWORD', variable: 'SPRING_MAIL_PASSWORD'),
                    string(credentialsId: 'TOSS_SECRET_KEY', variable: 'TOSS_SECRET_KEY')
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
                        echo "app_password=${app_password}" >> .env
                        echo "ACCESS_AWS=${ACCESS_AWS}" >> .env
                        echo "CLIENT_ID=${CLIENT_ID}" >> .env
                        echo "CLIENT_SECRET=${CLIENT_SECRET}" >> .env
                        echo "SECRET_AWS=${SECRET_AWS}" >> .env
                        echo "USER_POOL_ID=${USER_POOL_ID}" >> .env
                        echo "COGNITO_REDIRECT_URI=${COGNITO_REDIRECT_URI}" >> .env
                        echo "COGNITO_TOKEN_ENDPOINT=${COGNITO_TOKEN_ENDPOINT}" >> .env
                        echo "COGNITO_JWK_SET_URI=${COGNITO_JWK_SET_URI}" >> .env
                        echo "SPRING_MAIL_HOST=${SPRING_MAIL_HOST}" >> .env
                        echo "SPRING_MAIL_PORT=${SPRING_MAIL_PORT}" >> .env
                        echo "SPRING_MAIL_USERNAME=${SPRING_MAIL_USERNAME}" >> .env
                        echo "SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD}" >> .env
                        echo "TOSS_SECRET_KEY=${TOSS_SECRET_KEY}" >> .env

                        scp -o StrictHostKeyChecking=no -i /var/lib/jenkins/.ssh/id_rsa .env ubuntu@${EC2_IP}:${ENV_PATH}
                    """
                }
            }
        }

        stage('Checkout SCM') {
            steps {
                cleanWs()
                echo "✅ Checking out source code from GitHub..."
                checkout scm
            }
        }

        stage('Build & Deploy only if on develop branch') {
            when {
                allOf {
                    branch 'develop'
                }
            }

            steps {
                echo "✅ Deploying develop branch build..."

                script {
                    sh '''
                    docker build -t $ECR_REPO:$IMAGE_TAG .
                    docker tag $ECR_REPO:$IMAGE_TAG $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    '''
                }

                script {
                    sh '''
                    aws ecr get-login-password --region $AWS_REGION \
                        | docker login --username AWS --password-stdin $ECR_REGISTRY
                    docker push $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    '''
                }

                script {
                    // 포트 80 점유 중인지 사전 체크
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP '
                        if lsof -i:80 -sTCP:LISTEN -t >/dev/null; then
                            echo "❌ 포트 80 이미 사용 중 - 배포 중단"; exit 1;
                        fi
                    '
                    '''

                    // SSH 호스트 키 등록
                    sh '''
                    ssh-keyscan -H $EC2_IP >> ~/.ssh/known_hosts
                    '''

                    // Docker 네트워크 cluvr-net 생성 (있으면 스킵)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "docker network create cluvr-net 2>/dev/null || echo 'Network already exists'"
                    '''

                    // RabbitMQ만 상태 확인 및 실행
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP '
                        echo "🔍 의존성 서비스들 상태 확인 중..."

                        if [ -z "$(docker ps -q -f name=rabbitmq)" ]; then
                            echo "📦 RabbitMQ 시작 중..."
                            docker run -d --name rabbitmq --network cluvr-net -p 5672:5672 -p 15672:15672 --restart unless-stopped \
                                -e RABBITMQ_DEFAULT_USER=${RMQ_USERNAME} \
                                -e RABBITMQ_DEFAULT_PASS=${RMQ_PASSWORD} \
                                rabbitmq:3-management
                        else
                            echo "✅ RabbitMQ 이미 실행 중 - 스킵"
                        fi

                        echo "✅ 의존성 서비스 준비 완료!"
                    '
                    '''

                    // 기존 앱 중지 및 제거
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🔄 기존 cluvr-api 앱 중지 중...'
                        docker stop cluvr-api 2>/dev/null || true
                        docker rm cluvr-api 2>/dev/null || true
                        echo '✅ 기존 앱 정리 완료'
                    "
                    '''

                    // 최신 이미지 가져오기
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🔐 ECR 로그인 중...'
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                        echo '📥 최신 이미지 다운로드 중...'
                        docker pull $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    "
                    '''

                    // 앱 컨테이너 실행
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
                        echo '📍 접속 주소: http://$EC2_IP'
                    "
                    '''
                }
            }
        }
    }
}
