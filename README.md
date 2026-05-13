# E-commerce Platform JPA

Workshop project for building the foundation of an e-commerce system with Spring Boot, Spring Data JPA, and one-to-one entity relationships.

## Part 1 Scope

This part focuses on the relationship between a customer and their secondary data:

- `Customer`: primary user/customer entity
- `Address`: shipping or billing address
- `UserProfile`: optional extra customer details

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

## Domain Model

### Customer

Mapped to the `customers` table.

Fields:

- `id`
- `firstName`
- `lastName`
- `email`
- `createdAt`
- `address`
- `profile`

Relationships:

- One-to-one with `Address`
- Optional bidirectional one-to-one with `UserProfile`
- Cascades persistence operations to related address/profile records
- Uses orphan removal for removed related entities

### Address

Mapped to the `addresses` table.

Fields:

- `id`
- `street`
- `city`
- `zipCode`

Relationship:

- Standalone entity
- Referenced by `Customer` through `address_id`

### UserProfile

Mapped to the `user_profiles` table.

Fields:

- `id`
- `nickname`
- `phoneNumber`
- `bio`
- `customer`

Relationship:

- Inverse side of the bidirectional one-to-one relationship with `Customer`
- Uses `mappedBy = "profile"`

## Database Schema

Hibernate generates the schema from the JPA mappings.

Tables:

- `addresses`
- `user_profiles`
- `customers`

Foreign keys:

- `customers.address_id` references `addresses.id`
- `customers.profile_id` references `user_profiles.id`

## Repositories

### CustomerRepository

Includes queries for:

- Find by email
- Find by last name ignoring case
- Find by address city
- Search email by keyword
- Find by creation date range
- Count customers by city
- Check if email exists

### AddressRepository

Includes queries for:

- Find by zip code
- Find by city
- Find by street name
- Find by zip code prefix
- Count customers by zip code

### UserProfileRepository

Includes queries for:

- Find by nickname
- Search by partial phone number
- Find profiles with bio
- Find nicknames by prefix
- Count profiles by phone number prefix

## Run Locally

Start the application:

```bash
mvn spring-boot:run
```

Run tests:

```bash
mvn test
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

## Git

Current workshop branch:

```bash
feature/jpa-part1
```
