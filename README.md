# Delivery API

This is a simple Spring Boot App for an API that calculates the cost of delivery based on the parcel's weight and volume. The API also supports applying discount codes.

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

## Test the API

```
curl --location 'http://localhost:8080/delivery/cost' \
--header 'Content-Type: application/json' \
--data '{
    "weight": "10",
    "height": "10",
    "width": "10",
    "length": "10",
    "voucher_code": "MYNT"
}'
```

# API documentation

API documentation is available at `http://localhost:8080/swagger-ui.html`.

# Nice to have
- API authentication (OAuth, JWT, Basic Auth)
- Caching mechanism to reduce querying from DB