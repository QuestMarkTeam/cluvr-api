pipeline {
    agent any
    environment {
        AWS_REGION = 'us-west-2'
        AWS_ACCOUNT_ID = '617373894870'
        ECR_REPO = 'cluvr-api'
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGE_TAG = 'latest'
        EC2_IP = '44.239.99.137'
    }

    stages {
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
                    // 1. 네트워크가 없으면 생성 (이미 있으면 무시)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP "docker network create cluvr-net 2>/dev/null || echo 'Network already exists'"
                    '''

                    // 2. 의존성 서비스들이 실행 중인지 확인하고 없으면 시작 (이미 실행중이면 스킵)
                    sh '''
                    ssh -i /var/lib/jenkins/.ssh/id_rsa ubuntu@$EC2_IP '
                        echo "🔍 의존성 서비스들 상태 확인 중..."

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
                        docker run -d --name cluvr-api --network cluvr-net -p 8082:8082 --restart unless-stopped $ECR_REGISTRY/$ECR_REPO:$IMAGE_TAG
                        echo '🎉 배포 완료! 앱이 실행 중입니다.'
                        echo '📍 접속 주소: http://44.239.99.137'
                    "
                    '''
                }
            }
        }
    }
}