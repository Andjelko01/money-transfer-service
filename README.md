# Money Transfer Service

## Overview

This is a RESTful money transfer service written in Java using the Spring Boot framework. The service allows creating user accounts, checking balances, and performing money transfers between accounts. It implements robust error handling, concurrency control, and a full transaction audit log.

## Tech Stack

*   **Java 21**
*   **Spring Boot 3**
*   **Spring Data JPA (Hibernate)**
*   **PostgreSQL** as the database
*   **Flyway** for database migrations
*   **Docker & Docker Compose** for containerization
*   **Maven** for dependency management and build
*   **Swagger (OpenAPI)** for API documentation and testing
*   **Lombok** to reduce boilerplate code

## Running the Project

This project is fully containerized and easy to run with a single command.

### Prerequisites

*   **Docker**
*   **Docker Compose**

### Running the Application

From the root directory of the project, run the following command:

```bash
docker compose up --build
```

This command will:
1.  Build the Docker image for the `money-transfer-service`.
2.  Start the PostgreSQL database container.
3.  Start the application container.

Flyway migrations will run automatically on startup, setting up the database schema. The service will be available at `http://localhost:8080`.

## API Documentation (Swagger UI)

Once the application is running, interactive API documentation is available via Swagger UI.

**URL:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

You can use this interface to explore all available endpoints, view their parameters and responses, and test them directly from your browser.

## Database Migrations (Flyway)

The database schema is managed by Flyway. Hibernate is configured only to `validate` the schema against the entity models, not to alter it.

*   **Scripts Location:** All SQL migration scripts are located in `src/main/resources/db/migration`.
*   **Naming Convention:** Scripts must follow the `V<VERSION>__<Description>.sql` format (e.g., `V1__Initial_schema.sql`).
*   **Execution:** Migrations are applied automatically when the application starts.

To manually run migrations using the Maven plugin, first ensure your environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`) are set, then execute:

```bash
mvn flyway:migrate
```
