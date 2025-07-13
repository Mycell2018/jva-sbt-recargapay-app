# 💰 Wallet Service

A secure and traceable wallet microservice for managing user balances, transactions, and auditing operations.

---

## 🚀 Features

- ✅ Create wallet
- ✅ Retrieve current balance
- ✅ Retrieve historical balance
- ✅ Deposit, withdraw, and transfer funds
- ✅ Audit trail for all operations

---

## 🧱 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security with Keycloak
- Spring Data JPA
- Hibernate Envers
- PostgreSQL
- Lombok
- Swagger / OpenAPI 3.1

---

## ⚙️ Requirements

- Java 21+
- Maven 3.8+
- PostgreSQL 13+
- Docker (optional for DB)
- Keycloak instance (for authentication)

---

## 📦 Installation

### 1. Clone the repository

```bash
git clone https://github.com/Mycell2018/jva-sbt-recargapay-app.git
cd jva-sbt-recargapay-app
```

### 2. Start PostgreSQL (optional via Docker)

```bash
docker run --name recargapay_db \
  -e POSTGRES_DB=recargapay_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=o49CJ645xMl15nsSgeV7c9Lm \
  -p 5432:5432 \
  -d postgres:13
```

### 3. Configure `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/recargapay_db
    username: admin
    password: o49CJ645xMl15nsSgeV7c9Lm

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://sso.3fautomacao.com.br/realms/master
```

---

## 🔐 Security & Authentication

This service uses **Spring Security** integrated with **Keycloak** for OAuth2-based access control.

To obtain an access token:

```bash
curl --request POST \
  --url 'https://sso.3fautomacao.com.br/realms/master/protocol/openid-connect/token' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data grant_type=password \
  --data username=api \
  --data password=1234 \
  --data client_id=account
```

Include the token in every request:

```http
Authorization: Bearer <access_token>
```

---

## 📋 Auditing

This service uses **Hibernate Envers** to log and trace changes to all transactional and user data.  
You can view audit logs through the `/api/v1/audit` endpoints, as documented in Swagger.

---

## ▶️ Running the Application

```bash
mvn spring-boot:run
```

Application will be available at:

```
http://localhost:8080
```

---

## 🧪 Running Tests

To run all unit and integration tests:

```bash
mvn test
```

---

## 📖 API Documentation

You can also test the API online at:  
👉 [https://recargapay.3fautomacao.com.br/swagger-ui/index.html#/](https://recargapay.3fautomacao.com.br/swagger-ui/index.html#/)



Swagger UI and OpenAPI spec are provided:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/docs/openapi.json](http://localhost:8080/docs/openapi.json)
- **HTML Docs (Redoc):** [http://localhost:8080/openapi-doc.html](http://localhost:8080/openapi-doc.html)

---

## 🧭 Example Endpoints

| Method | Endpoint                                        | Description                         |
|--------|-------------------------------------------------|-------------------------------------|
| POST   | `/api/v1/users`                                 | Create new user                     |
| GET    | `/api/v1/users/{id}`                            | Get user by ID                      |
| POST   | `/api/v1/accounts/user/{userId}`                | Create bank account for a user      |
| GET    | `/api/v1/accounts/{accountId}/balance`          | Get current balance                 |
| GET    | `/api/v1/accounts/{accountId}/balance-at?at=`   | Get historical balance              |
| POST   | `/api/v1/accounts/{accountId}/deposit`          | Deposit funds                       |
| POST   | `/api/v1/accounts/{accountId}/withdraw`         | Withdraw funds                      |
| POST   | `/api/v1/accounts/{from}/transfer/{to}`         | Transfer funds between accounts     |
| GET    | `/api/v1/audit/{entity}/{id}`                   | Get audit history for an entity     |

---

## 🕐 Time Spent

**Estimated time to complete:** ~7 hours  
(including architecture, implementation, testing, documentation)

---

## 📄 License

This project is **strictly confidential** and provided for evaluation purposes only.


---

## 🧠 Design Decisions

The Wallet Service was designed following the principles of modularity, traceability, and reliability.

- Each core function (create wallet, deposit, withdraw, transfer) is encapsulated in its own service with proper domain boundaries.
- Hibernate Envers is used to ensure full auditability of financial and user-related actions.
- Spring Security and OAuth2 integration with Keycloak guarantees token-based access control, enabling secure communication with minimal overhead.
- PostgreSQL was selected due to its strong ACID compliance and support for concurrent transactions.
- All endpoints are idempotent where applicable, and transactional integrity is guaranteed via `@Transactional` boundaries in service layers.

This design ensures that the service fully satisfies:
- Functional requirements for balance management and fund movement.
- Non-functional requirements such as availability, consistency, and traceability.

---

## ⏳ Trade-offs & Time-Based Compromises

Given the 6–8 hour scope constraint, the following compromises were made:

- The system currently supports one wallet per user. Multi-wallet support could be implemented later via an extra mapping table or multi-tenancy layer.
- Some validations are handled at the service level and not yet enforced through a centralized validation mechanism.
- The deployment strategy was not containerized (e.g., via Docker Compose), although the system is compatible with such an approach.
- Integration with external services (notifications, fraud prevention) was excluded to keep the scope focused and manageable.

These trade-offs were carefully chosen to maintain a working and testable service that delivers on all core features while remaining easy to extend.
