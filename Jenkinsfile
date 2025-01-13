pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = credentials('github-credentials-id')
        GIT_BRANCH = 'dev'
        GITHUB_REPO_URL = 'https://github.com/FitAble-Org/fitable-backend.git'
        DOCKER_IMAGE_NAME = 'fitable'
        DOCKER_CONTAINER_NAME = 'fitable-container'
        DOCKER_PORT = '8081'

        DB_URL = credentials('DB_URL')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
    }

    triggers {
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
                    sh 'chmod +x ./gradlew'

                    withCredentials([
                        string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                        string(credentialsId: 'OPENAI_API_KEY', variable: 'OPENAI_API_KEY'),
                        string(credentialsId: 'NAVER_CLIENT_ID', variable: 'NAVER_CLIENT_ID'),
                        string(credentialsId: 'NAVER_CLIENT_SECRET', variable: 'NAVER_CLIENT_SECRET')
                    ]) {
                        sh """
                            ./gradlew clean build -x test \
                            -Djwt.secret-key=$JWT_SECRET_KEY \
                            -Dopenai.api.key=$OPENAI_API_KEY \
                            -Dnaver.client-id=$NAVER_CLIENT_ID \
                            -Dnaver.client-secret=$NAVER_CLIENT_SECRET
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImageName = "jaegyeong223/fitable" // Docker Hub 레포지토리 경로

                    withCredentials([usernamePassword(
                        credentialsId: 'docker-hub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )]) {
                        // Docker Hub 로그인
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"

                        // Docker 이미지 빌드 및 푸시
                        sh """
                        docker buildx create --use || true
                        docker buildx build \
                            --platform linux/amd64,linux/arm64 \
                            --build-arg SPRING_REDIS_HOST=172.17.0.2 \
                            --build-arg SPRING_REDIS_PORT=6379 \
                            -t ${dockerImageName}:latest \
                            --push .
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                        string(credentialsId: 'OPENAI_API_KEY', variable: 'OPENAI_API_KEY'),
                        string(credentialsId: 'NAVER_CLIENT_ID', variable: 'NAVER_CLIENT_ID'),
                        string(credentialsId: 'NAVER_CLIENT_SECRET', variable: 'NAVER_CLIENT_SECRET')
                    ]) {
                        // Base64 인코딩 및 Kubernetes secrets 생성
                        def secretData = [
                            JWT_SECRET_KEY: env.JWT_SECRET_KEY,
                            OPENAI_API_KEY: env.OPENAI_API_KEY,
                            NAVER_CLIENT_ID: env.NAVER_CLIENT_ID,
                            NAVER_CLIENT_SECRET: env.NAVER_CLIENT_SECRET,
                            DB_URL: env.DB_URL,
                            DB_USERNAME: env.DB_USERNAME,
                            DB_PASSWORD: env.DB_PASSWORD
                        ].collectEntries { key, value ->
                            [(key): sh(script: "echo -n '${value}' | base64 -w 0", returnStdout: true).trim()]
                        }

                        // secrets.yaml 생성
                        writeFile file: 'k8s/secrets-applied.yaml', text: """
                        apiVersion: v1
                        kind: Secret
                        metadata:
                          name: fitable-secrets
                        data:
                          JWT_SECRET_KEY: ${secretData.JWT_SECRET_KEY}
                          OPENAI_API_KEY: ${secretData.OPENAI_API_KEY}
                          NAVER_CLIENT_ID: ${secretData.NAVER_CLIENT_ID}
                          NAVER_CLIENT_SECRET: ${secretData.NAVER_CLIENT_SECRET}
                          DB_URL: ${secretData.DB_URL}
                          DB_USERNAME: ${secretData.DB_USERNAME}
                          DB_PASSWORD: ${secretData.DB_PASSWORD}
                        """

                        // Kubernetes 리소스 적용
                        sh """
                        kubectl apply -f k8s/namespace.yaml
                        kubectl apply -f k8s/secrets-applied.yaml
                        kubectl apply -f k8s/redis.yaml
                        kubectl apply -f k8s/fitable-app.yaml
                        kubectl apply -f k8s/nginx.yaml
                        kubectl apply -f k8s/ingress.yaml
                        """
                    }
                }
            }
        }
    }
}
