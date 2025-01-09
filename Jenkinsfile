pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = credentials('github-credentials-id')
        GIT_BRANCH = 'dev'
        GITHUB_REPO_URL = 'https://github.com/FitAble-Org/fitable-backend.git'
        DOCKER_IMAGE_NAME = 'fitable'
        DOCKER_CONTAINER_NAME = 'fitable-container'
        DOCKER_PORT = '8081'  // 8081 포트로 설정

        // Database credentials
        DB_URL = credentials('DB_URL') // Jenkins에 저장된 DB URL
        DB_USERNAME = credentials('DB_USERNAME')  // Jenkins에 저장된 DB 사용자 이름 자격증명 ID
        DB_PASSWORD = credentials('DB_PASSWORD')  // Jenkins에 저장된 DB 비밀번호 자격증명 ID

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
                    sh 'chmod +x ./gradlew'

                    withCredentials([
                        string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                        string(credentialsId: 'OPENAI_API_KEY', variable: 'OPENAI_API_KEY'),
                        string(credentialsId: 'NAVER_CLIENT_ID', variable: 'NAVER_CLIENT_ID'),
                        string(credentialsId: 'NAVER_CLIENT_SECRET', variable: 'NAVER_CLIENT_SECRET')
                    ]) {
                        // Gradle 빌드 실행, 테스트 단계 건너뛰기
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
                    // 도커 이미지명 정의
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
                        // 쿠버네티스 디플로이먼트 적용
                        sh """
                        kubectl apply -f k8s/deployment.yaml
                        kubectl apply -f k8s/service.yaml
                        kubectl apply -f k8s/ingress.yaml
                        """
                    }
                }
            }
        }
    }
}
