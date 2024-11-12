pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = credentials('github-credentials-id')  // Jenkins에 저장된 자격증명 ID
        GIT_BRANCH = 'dev'
        GITHUB_REPO_URL = 'https://github.com/FitAble-Org/fitable-backend.git'
        DOCKER_IMAGE_NAME = 'fitable'
        DOCKER_CONTAINER_NAME = 'fitable-container'
        DOCKER_PORT = '8080'

        // Database credentials
        DB_URL = 'jdbc:mysql://localhost:3306/fitable'
        DB_USERNAME = credentials('db-username-id')  // Jenkins에 저장된 DB 사용자 이름 자격증명 ID
        DB_PASSWORD = credentials('db-password-id')  // Jenkins에 저장된 DB 비밀번호 자격증명 ID
    }

    triggers {
        // GitHub 웹훅 트리거
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: "${GIT_BRANCH}", credentialsId: "${GITHUB_CREDENTIALS_ID}", url: "${GITHUB_REPO_URL}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // gradlew 파일에 실행 권한 부여
                    sh 'chmod +x ./gradlew'

                    // JWT 및 OpenAI API 키를 안전하게 처리하기 위해 withCredentials 블록 사용
                    withCredentials([
                        string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                        string(credentialsId: 'OPENAI_API_KEY', variable: 'OPENAI_API_KEY')
                    ]) {
                        // Gradle 빌드 실행
                        sh """
                            ./gradlew clean build \
                            -Djwt.secret-key=$JWT_SECRET_KEY \
                            -Dopenai.api.key=$OPENAI_API_KEY
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // 도커 이미지명 정의
                    def dockerImageName = env.DOCKER_IMAGE_NAME ?: 'fitable'
                    sh "docker build -t ${dockerImageName}:latest ."
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // 도커 컨테이너명 정의
                    def dockerContainerName = env.DOCKER_CONTAINER_NAME ?: 'fitable-container'
                    def dockerPort = env.DOCKER_PORT ?: '8080'

                    // 기존 컨테이너를 중지하고 삭제한 후 새 컨테이너 실행
                    sh """
                    docker stop ${dockerContainerName} || true
                    docker rm ${dockerContainerName} || true
                    docker run -d -p 80:${dockerPort} \
                      -e DB_URL=${DB_URL} \
                      -e DB_USERNAME=${DB_USERNAME} \
                      -e DB_PASSWORD=${DB_PASSWORD} \
                      -e JWT_SECRET_KEY=$JWT_SECRET_KEY \
                      -e OPENAI_API_KEY=$OPENAI_API_KEY \
                      --name ${dockerContainerName} ${env.DOCKER_IMAGE_NAME}:latest
                    """
                }
            }
        }
    }
}