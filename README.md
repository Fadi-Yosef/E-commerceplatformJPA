# E-commerce Platform JPA

Workshop project for building the foundation of an e-commerce system with Spring Boot, Spring Data JPA, entity relationships, and repositories.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Web
- Spring Validation
- H2 Database
- MySQL Driver
- Lombok
- Maven

## Part 1 Scope

Part 1 introduces customer data and one-to-one relationships.

Entities:

- `Customer`
- `Address`
- `UserProfile`

Relationships:

- `Customer` has one mandatory `Address`.
- `Customer` has one optional `UserProfile`.
- Address and profile records cascade with the customer and use orphan removal.

Repositories:

- `CustomerRepository`
- `AddressRepository`
- `UserProfileRepository`

## Database Schema

Hibernate generates the schema from the JPA mappings.

Tables:

- `addresses`
- `user_profiles`
- `customers`

## Verification

Run tests:

```bash
mvn test
```

Start the application:

```bash
mvn spring-boot:run
```

If port `8080` is already in use, run with a random port:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=0
```

The application uses an in-memory H2 database by default.

H2 console:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:mem:ecommerce
```

Username:

```text
sa
```

Password is empty.

## Part 1 Submission Checklist

- [x] Git Branch: Created `feature/jpa-part1`.
- [x] Entities: Added `Customer`, `Address`, and `UserProfile` with JPA annotations.
- [x] Relationships: Implemented one-to-one mappings with cascading and orphan removal.
- [x] Repositories: Added `CustomerRepository`, `AddressRepository`, and `UserProfileRepository` with query methods.
- [x] Verification: Ran tests and verified application startup and schema generation.
- [x] Commits: Created descriptive commits for entity mappings and repositories.
- [x] Push: Pushed `feature/jpa-part1` to GitHub.

## Git

Current workshop branch:

```bash
feature/jpa-part1
```

Pull request link:

```text
https://github.com/Fadi-Yosef/E-commerceplatformJPA/pull/new/feature/jpa-part1
```
