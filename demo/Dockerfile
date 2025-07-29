# 1. OpenJDK 17 경량 이미지 기반
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. APM Agent JAR 복사 (현재 경로에 agent가 있다고 가정)
COPY elastic-apm-agent.jar /app/elastic-apm-agent.jar

# 4. 빌드된 Spring Boot JAR 복사
COPY target/*.jar /app/app.jar

# 5. 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java \
  -javaagent:/app/elastic-apm-agent.jar \
  -Delastic.apm.service_name=my-springboot-apm-app \
  -Delastic.apm.server_urls=${ELASTIC_APM_SERVER_URLS} \
  -Delastic.apm.secret_token=${ELASTIC_APM_SECRET_TOKEN} \
  -Delastic.apm.application_packages=com.example.demo \
  -Delastic.apm.environment=prd \
  -Delastic.apm.enable_log_correlation=true \
  -jar app.jar"]
