# Customer Rewards Program API

This Spring Boot project calculates customer rewards based on purchases over a 3-month period.

## Features

- 🎯 Reward logic:
    - 1 point for $51-$100
    - 2 points per $1 over $100
- 🧾 Fetch rewards by customer or all customers
- 📊 Transaction filtering and pagination
- 🧪 Error handling and logging
- 📘 Swagger UI documentation
- 📦 API Versioning (`/v1`)

## API Endpoints

| Method | Endpoint                 | Description                       |
|--------|--------------------------|-----------------------------------|
| POST   | /v1/transactions/add     | Add a Transaction                 |
| GET    | /v1/transactions         | List all transactions             |
| GET    | /v1/rewards              | Get rewards for all customers     |
| GET    | /v1/rewards/{customerId} | Get rewards for specific customer |


Swagger: http://localhost:8080/swagger-ui/index.html

## Run Locally

```bash
mvn spring-boot:run

