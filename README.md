# Delivery API

# Requirements
- Java 21
- Docker

# Getting started

## Spin up Docker

This will automatically create the DDB tables and insert test data.

```
docker-compose up -d
```

## run via IDE with Spring plugin

1. Import as maven project.
2. Run as Spring Boot application.

## run via IDE without Spring plugin

1. Import as maven project.
2. Run `DeliveryApplication.java`.

## run via command line

```
cd how-much
./mvnw spring-boot:run
```

## run tests via command line

```
cd how-much
./mvnw test
```

# Nice to have
- API authentication (OAuth, JWT, Basic Auth)
- Caching mechanism to reduce querying from DB