# springboot-apm-demo

Spring Boot 프로젝트로, Elastic APM 연동 및 다양한 예외 상황을 테스트할 수 있는 간단한 데모입니다.

---

## 🚀 주요 기능

- 콘솔 로그 출력 (SLF4J + Logback)
- Elastic APM 연동
- 다양한 예외 발생 API (RuntimeException, NullPointerException, CustomException 등)

---

## 🛠️ 실행 방법

### 1. Elastic APM Agent 다운로드

```bash
curl -o elastic-apm-agent.jar -L 'https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=co.elastic.apm&a=elastic-apm-agent&v=LATEST'
```

### 2. IntelliJ에서 실행 설정
IntelliJ의 Run > Edit Configurations에서 VM options에 아래 항목 추가:
```bash
-javaagent:./elastic-apm-agent.jar
-Delastic.apm.service_name=springboot-apm-demo
-Delastic.apm.server_url=https://your-apm-url:8200
-Delastic.apm.secret_token=your-secret-token
-Delastic.apm.environment=dev
-Delastic.apm.application_packages=com.example.demo
```
### 3. 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

### 참고) 요구사항
Java 17 이상
Maven 3.8+ (권장)


# 호출
```bash
curl http://localhost:8080/start
```
