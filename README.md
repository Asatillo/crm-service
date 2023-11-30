# CRM API Documentation 

## Table of Contents
### [1. Introduction](#introduction)
### [2. How to Set Up and Run the CRM API](#how-to-set-up-and-run-the-crm-api)
### [3. API Documentation](#api-documentation)

## Introduction
This is a CRM API that allows you to manage customers and their subscriptions. It is built with Java 17, Spring Boot, 
Spring Data JPA, and MySQL. It is a RESTful API that uses JSON for data exchange. It is also a Dockerized application 
that can be run in a Docker container. The CRM API manages what kind of subscription the customer has, and create 
update or remove subscription plans. Each customer might have many phone numbers, with each being able to have different
subscription plans. Subscriptions have expiration dates and if customers want may be extended. Each plan has 
different service varieties such as data, voice, or SMS. If a customer has a subscription he might purchase a mobile 
phone from the company. Routers are provided for the wired internet.
## How to Set Up and Run the CRM API

### Prerequisites:
- Docker
- IDE for code editing (optional)

### Steps:
#### 1) Clone the Repository to Your Local Machine:
```bash
git clone https://github.com/Asatillo/CRM_API.git
cd CRM_API
```

#### 2) Create a .env File in the root directory of the project and set the following environment variables:
```
MYSQLDB_USER=[your_mysql_user]
MYSQLDB_ROOT_PASSWORD=[your_mysql_root_password]
MYSQLDB_DATABASE=crm
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=6868
SPRING_DOCKER_PORT=8080
```
Replace `[your_mysql_user]` and `[your_mysql_root_password]` with your desired values. Make sure the required ports are not already in use on your machine.

#### 3) Build and Run Docker Containers:
Build and run MySQL and CRM containers.
```bash
docker-compose up --build
```
To run without building(in case it was already built):
```bash
docker-compose up
```

### 4) Access the Application:
Explore API documentation at http://localhost:6868/api/swagger-ui.html.

Access the CRM app endpoints at http://localhost:6868/api.

### 5) Stop and Remove Containers:
Use docker-compose down to stop and remove containers.
```bash
docker-compose down
```

## API Documentation

All documentation for API is available at http://localhost:6868/api/swagger-ui.html.