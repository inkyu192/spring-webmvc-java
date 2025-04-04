## 개발 환경
- **Language & Framework:** Java, Spring Web MVC, Spring Security, Spring Data JPA, QueryDSL, Spring REST Docs
- **Database:** MySQL, Redis
- **Test & Tools:** JUnit5, Mockito, Docker, Docker Compose, Gradle

---

## 환경 설정
`docker-compose.yml`을 사용하여 MySQL, Redis를 실행할 수 있습니다.

```yaml
services:
  postgres:
    container_name: postgres-container
    image: postgres:latest
    environment:
      POSTGRES_DB: my_db
      POSTGRES_USER: my_user
      POSTGRES_PASSWORD: my_password
    ports:
      - "5432:5432"
    networks:
      - application-network

  redis:
    image: redis:latest
    ports:
      - "6379:6379"
```

---

## API 문서
- 이 프로젝트는 **Spring REST Docs**를 사용하여 API 문서를 생성합니다.
- 먼저 빌드를 수행해야 문서가 생성됩니다.

```bash
./gradlew build
```
- 서버 실행 시 아래 경로에서 문서를 확인할 수 있습니다.

```text
http://localhost:8080/docs/index.html
```

---

## 아키텍처
```sh
┌── application  # 애플리케이션 계층  
│  
├── domain  # 도메인 계층  
│  
├── infrastructure  # 인프라스트럭처 계층  
│  
└── presentation  # 프레젠테이션 계층  
```
- **application (애플리케이션 계층):** 사용자 요청을 처리하고 도메인 계층을 활용합니다.
- **domain (도메인 계층):** 비즈니스 규칙과 도메인 모델을 관리합니다.
- **infrastructure (인프라스트럭처 계층):** 외부 시스템, DB, 메시징, 설정 등 기술적 세부 사항을 관리합니다.
- **presentation (프레젠테이션 계층):** 클라이언트 요청을 수신하고 애플리케이션 계층에 전달합니다.
