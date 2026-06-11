# E-commerce Platform JPA

Workshop project for building an e-commerce system with Spring Boot, Spring Data JPA, entity relationships, repositories, and startup data seeding.

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

## Part 2 Scope

Part 2 adds catalog management, order transactions, promotions, repositories, and startup seed data.

Entities and enum:

- `Category`
- `Product`
- `Promotion`
- `Order`
- `OrderItem`
- `OrderStatus`

Relationships:

- `Category` one-to-many `Product`.
- `Product` many-to-one `Category` using `category_id`.
- `Product` many-to-many `Promotion` through `products_promotions`.
- `Customer` one-to-many `Order`.
- `Order` many-to-one `Customer` using `customer_id`.
- `Order` one-to-many `OrderItem` with cascade and orphan removal.
- `OrderItem` many-to-one `Product` using `product_id`.
- `OrderStatus` is stored as a readable string.

Important mapping choices:

- Reference relationships use explicit lazy fetching.
- `Order.items` is the inverse side with `mappedBy = "order"`.
- `OrderItem.order` owns the `order_id` foreign key.
- `Product` owns the product-promotion join table.
- `CascadeType.ALL` is avoided for product-promotion many-to-many relationships.
- `Order` validates that at least one `OrderItem` exists before saving.
- `OrderItem.priceAtPurchase` stores the historical price at checkout time instead of depending on the current product price.

## Repositories

Part 2 repositories:

- `CategoryRepository`
- `ProductRepository`
- `OrderRepository`
- `OrderItemRepository`
- `PromotionRepository`

Query coverage:

- Category lookup by name, existence check, and keyword search.
- Product lookup by category name, category id, price range, keyword, cheaper-than price, sorting, and category count.
- Order lookup by customer id, status, date ranges, contained product, status count, and customer/status.
- `OrderRepository.findByStatus(...)` uses `@EntityGraph` to load order items and avoid the basic N+1 problem.
- Promotion lookup by active date, code, start date, end date, no end date, and active today.
- Order item lookup by order id, product id, and quantity threshold.

## Data Seeding

`DataSeeder` runs automatically when the application starts.

Seeded categories:

- Electronics
- Books
- Home
- Clothing

Seeded products are linked to existing categories. The seeder checks for existing category and product names before inserting, so rerunning the application does not create duplicates.

## Database Schema

Hibernate generates the schema from the JPA mappings.

Main tables:

- `addresses`
- `user_profiles`
- `customers`
- `categories`
- `products`
- `product_images`
- `promotions`
- `products_promotions`
- `orders`
- `order_items`

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

## Part 2 Submission Checklist

- [x] Git Branch: Created `feature/jpa-part2`.
- [x] Entities & Enums: Added the required catalog, promotion, order, order item, and status model.
- [x] Relationships: Implemented many-to-one, one-to-many, and many-to-many mappings with proper ownership and cascading.
- [x] Repositories: Added required Spring Data JPA repositories and query methods.
- [x] N+1 Strategy: Used `@EntityGraph` for loading orders by status with items.
- [x] Extra Task: Added automatic startup data seeding.
- [x] Verification: Ran tests and verified application startup, schema generation, and seed inserts.
- [x] Commits: Created descriptive commits for entity mappings, repositories, seeding, and documentation.
- [x] Push: Pushed `feature/jpa-part2` to GitHub.

## Git

Current workshop branch:

```bash
feature/jpa-part2
```

Pull request link:

```text
https://github.com/Fadi-Yosef/E-commerceplatformJPA/pull/new/feature/jpa-part2
```
