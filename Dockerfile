# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# JAR 파일을 이미지에 복사
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]