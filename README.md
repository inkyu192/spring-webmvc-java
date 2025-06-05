## 개발 환경
- **Language:** Java
- **Library / Framework:** Spring Web MVC, Spring Data JPA, Querydsl, Spring REST Docs
- **Database:** PostgreSQL, Redis, MongoDB
- **Test:** JUnit 5, Mockito, Testcontainers
- **Infrastructure**: Docker, Docker Compose, LocalStack

---

## 환경 설정
`docker-compose.yml`을 사용하여 PostgreSQL, Redis, LocalStack 실행할 수 있습니다.

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
    container_name: redis-container
    image: redis:latest
    ports:
      - "6379:6379"
    networks:
      - application-network

  mongodb:
    container_name: mongodb-container
    image: mongo:latest
    ports:
      - "27017:27017"
    networks:
      - application-network

  localstack:
    container_name: localstack-container
    image: localstack/localstack:latest
    ports:
      - "4566:4566"
    environment:
      - SERVICES=s3
      - DEBUG=1
      - AWS_ACCESS_KEY_ID=accessKey
      - AWS_SECRET_ACCESS_KEY=secretKey
      - DEFAULT_REGION=ap-northeast-2
    volumes:
      - ./init-localstack.sh:/etc/localstack/init/ready.d/init.sh
    networks:
      - application-network

networks:
  application-network:
    name: application-network
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
