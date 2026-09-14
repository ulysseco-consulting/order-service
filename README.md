# order-service

A small sample order management service built with Spring Boot 3, Spring Data JPA, Flyway and PostgreSQL.
It exposes a read API over customers, their orders and order line items.

All data in this repository is fictional sample data.

## Requirements

- Java 21
- Maven 3.9+
- Docker Desktop (for the local PostgreSQL database)

## Running locally

1. Start the database:

   ```bash
   docker compose up -d
   ```

2. Run the application (`OrderServiceApplication`) from your IDE, or:

   ```bash
   mvn spring-boot:run
   ```

   On first start, Flyway creates the schema and loads the sample data set
   (2,000 customers, ~100,000 orders). Customer `1` is a high-volume account with 2,000 orders.

3. Try it:

   ```bash
   curl http://localhost:8080/api/orders/1
   ```

### Seeing the SQL

Run with the `sqllog` profile to log every SQL statement and the number of JDBC statements per request:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=sqllog
```

## Tests

Tests run against an in-memory H2 database in PostgreSQL mode, so Docker is not required:

```bash
mvn test
```

## Database migrations

| Location | Purpose |
| --- | --- |
| `src/main/resources/db/migration` | Schema migrations (run everywhere, including tests) |
| `src/main/resources/db/seed` | Sample data for local development (PostgreSQL only) |

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/orders/{orderId}` | Order with line items and total |

## License

Released into the public domain under [CC0 1.0](LICENSE).
