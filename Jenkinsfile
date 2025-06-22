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
                    string(credentialsId: 'REDIS_PORT', variable: 'REDIS_PORT')
                ]) {
                    sh """
                        echo "JWT_SECRET_KEY=${JWT_SECRET_KEY}" > .env
                        echo "SPRING_DATASOURCE_URL=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8" >> .env
                        echo "SPRING_DATASOURCE_USERNAME=${DB_USERNAME}" >> .env
                        echo "SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}" >> .env
                        echo "REDIS_HOST=${REDIS_HOST}" >> .env
                        echo "REDIS_PORT=${REDIS_PORT}" >> .env

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
                branch 'develop'
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
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                        docker push $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                    '''
                }

                script {
                    // Docker network 생성
                    sh '''
                        ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "docker network create cluvr-net 2>/dev/null || echo 'Network already exists'"
                    '''

                    // RabbitMQ 실행만 확인
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
                        '
                    '''

                    // 기존 컨테이너 중지 및 제거
                    sh '''
                        ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                            docker stop cluvr-api 2>/dev/null || true
                            docker rm cluvr-api 2>/dev/null || true
                        "
                    '''

                    // 최신 이미지 풀
                    sh '''
                        ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                            aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                            docker pull $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                        "
                    '''

                    // 새 컨테이너 실행
                    sh '''
                        ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                            do
