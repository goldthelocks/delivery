# Delivery API

# Requirements
- Java 21
- Docker

# Getting started

## Spin up Docker

This will automatically create the `Rule` DDB table and insert test data.

```
docker-compose up -d
```

## Run Spring app

### run via IDE with Spring plugin

1. Import as maven project.
2. Run as Spring Boot application.

### run via IDE without Spring plugin

1. Import as maven project.
2. Run `DeliveryApplication.java`.

### run via command line

```
cd how-much
./mvnw spring-boot:run
```

# Nice to have
- API authentication (OAuth, JWT, Basic Auth)
- Caching mechanism to reduce querying from DB