# BankSimple

BankSimple is a simple banking application built with Spring Boot and PostgreSQL. It provides a RESTful API for managing bank accounts, users, and transactions.

## Features

- Create, read, update, and delete bank accounts
- Create, read, update, and delete bank transactions
- Authenticate and authorize users
- Validate account and transaction data
- Generate API documentation using Swagger

## Prerequisites

- Java 17 or later
- PostgreSQL 13 or later

## Getting Started

1. Clone this repository
2. Build the Docker image
3. Start the application using Docker Compose

## Package the application

```bash
mvn clean package
```


## Build the Docker Image

```bash
docker build -t bank-app .
```

## Start the Application using Docker Compose

```bash
docker-compose up -d
```

This will start your application in the background and map port 8080 on your local machine to port 8080 in the container. You can then access the API documentation at `http://localhost:8080/swagger-ui.html`.

Please note that you need to have Docker and Docker Compose installed on your machine to run this command.

# Application Authentication Details

To authenticate and access the protected endpoints of the application, please use the following credentials:

- **Username**: `john_doe`
- **Password**: `password`
