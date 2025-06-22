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
                        echo "REDIS_PORT=${RMQ_HOST}" >> .env
                        echo "REDIS_PORT=${RMQ_PORT}" >> .env
                        echo "REDIS_PORT=${RMQ_USERNAME}" >> .env
                        echo "REDIS_PORT=${RMQ_PASSWORD}" >> .env

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

                // Docker build & tag
                sh '''
                    docker build -t $ECR_REPO:$IMAGE_TAG .
                    docker tag $ECR_REPO:$IMAGE_TAG $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                '''

                // Push to ECR
                sh '''
                    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                    docker push $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                '''

                // Remote EC2 deploy steps
                sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "
                        echo '🔧 네트워크 생성 (있으면 생략)'
                        docker network create cluvr-net 2>/dev/null || echo 'Already exists'

                        echo '🐰 RabbitMQ 체크 및 실행'
                        if [ -z \$(docker ps -q -f name=rabbitmq) ]; then
                            docker run -d --name rabbitmq --network cluvr-net -p 5672:5672 -p 15672:15672 \
                                -e RABBITMQ_DEFAULT_USER=${RMQ_USERNAME} \
                                -e RABBITMQ_DEFAULT_PASS=${RMQ_PASSWORD} \
                                --restart unless-stopped rabbitmq:3-management
                        else
                            echo '✅ RabbitMQ 이미 실행 중'
                        fi

                        echo '🛑 기존 앱 중지'
                        docker stop cluvr-api 2>/dev/null || true
                        docker rm cluvr-api 2>/dev/null || true

                        echo '📦 이미지 pull'
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                        docker pull $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG

                        echo '🚀 새 앱 실행'
                        docker run -d --name cluvr-api --network cluvr-net -p 8082:8082 --restart unless-stopped \
                            --env-file ${ENV_PATH} $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG

                        echo '✅ 배포 완료: http://$EC2_IP:8082'
                    "
                '''
            }
        }
    }
}
