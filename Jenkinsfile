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
                    def dockerImageName = env.DOCKER_IMAGE_NAME ?: 'fitable'
                    sh "docker build -t ${dockerImageName}:latest ."
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
                        // Base64로 환경변수 인코딩
                        def jwtBase64 = sh(script: "echo -n $JWT_SECRET_KEY | base64", returnStdout: true).trim()
                        def openaiBase64 = sh(script: "echo -n $OPENAI_API_KEY | base64", returnStdout: true).trim()
                        def naverClientIdBase64 = sh(script: "echo -n $NAVER_CLIENT_ID | base64", returnStdout: true).trim()
                        def naverClientSecretBase64 = sh(script: "echo -n $NAVER_CLIENT_SECRET | base64", returnStdout: true).trim()
                        def dbUrlBase64 = sh(script: "echo -n $DB_URL | base64", returnStdout: true).trim()
                        def dbUsernameBase64 = sh(script: "echo -n $DB_USERNAME | base64", returnStdout: true).trim()
                        def dbPasswordBase64 = sh(script: "echo -n $DB_PASSWORD | base64", returnStdout: true).trim()

                        // secrets.yaml 파일 내용 동적 생성
                        sh """
                        sed -e 's/{{JWT_SECRET_KEY_BASE64}}/${jwtBase64}/g' \
                            -e 's/{{OPENAI_API_KEY_BASE64}}/${openaiBase64}/g' \
                            -e 's/{{NAVER_CLIENT_ID_BASE64}}/${naverClientIdBase64}/g' \
                            -e 's/{{NAVER_CLIENT_SECRET_BASE64}}/${naverClientSecretBase64}/g' \
                            -e 's/{{DB_URL_BASE64}}/${dbUrlBase64}/g' \
                            -e 's/{{DB_USERNAME_BASE64}}/${dbUsernameBase64}/g' \
                            -e 's/{{DB_PASSWORD_BASE64}}/${dbPasswordBase64}/g' \
                            k8s/secrets.yaml > k8s/secrets-applied.yaml
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
