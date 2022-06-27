# API for Mobile Developer Test

**CMC Developer - Tuan Ngo**

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Requirements](#requirements)
* [URL](#url)
* [Explanation](#explanation)
* [Notes](#note)

## General info

Web Service to register user from mobile platform via API

## Technologies

* Java 11
* Spring Boot 2.7.1
* PostgreSQL 42.3.0
* Spring Kafka

## Setup

**Prerequisites:**

- PostgreSQL running at port 5432 and has database 'testapi'
- As the system uses kafka as message broker, we need to spin up a minimal kafka cluster before
  starting the server
  Please Run Compose file located at _./docker-compose/docker-compose.yml_ using docker

``````
docker-compose up -d
``````

**Run:**

Preferably import the project into your favorite IDE and run the project.

## URL

``````
curl --location --request POST 'localhost:8081/api/auth/v1/registration' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test@gmail.com",
    "password": "12345678",
    "based_salary": 100000
}'
``````

- There are validations for the request body:
    - email must be of valid format
    - password must be at least of 8 character in length
    - based_salary must be at least 15000

## Requirements

- Create API user registration that accepts user input and create new user accordingly
- User Member Type will be classified based on input based_salary

## Explanation

The System was implemented to satisfy the requirements by client:

1. REST API development basics:
    - Using RestController to consume JSON request body and returning the HttpStatus properly when a
      resource is created (HttpStatus Code 201)
2. Design pattern and frameworks
    - Using Spring Framework (Spring Boot) to take advantage of best practices as well as
      community-proven libraries
    - Developing using microservice architecture: There are 2 microservices running (AuthService for
      registration process and UserService for user management) as for separation of concerns and
      also provide capabilities for further development.
    - The AuthService with user-facing user is implemented with 3-tier architecture to provide clean
      flow of code / data as well as better code organization
3. Data and error handling
    - A Validator library was added to provide validation at Controller level, before data enters
      the system
    - The Controller layer is also equipped with a Controller Advice to handle the exceptions
      separately.
      4 Software quality
    - Tests were included to make sure the code logic runs as expected
    - Spring Framework is chosen partly because of its opinionated and production-graded
      dependencies

## Note

In order to enhance the system, more components from Spring Cloud framework should be integrated to
provide a fully capable microservice architecture with resiliency and scalability.

- Service Discovery, in combination with an API Gateway, is a must to manage microservices instances
  and provide load balancing for the system. Solutions provided by Spring Cloud (Eureka, Spring
  Cloud Gateway) or cloud providers can be considered.
- For the moment, the 2 microservices are communicating using Apache Kafka which is known for its
  fault-tolerance and scalability.
- The System is expected to have multiple services and can depend on Kafka as its message broker.
  More complex and customized kafka clusters should be taken into consideration.
- Current usage of UserService as user management should be delegated to a separate Identity
  provider (e.g. Keycloak) to implement SSO and also better provision of identity management. This
  service can later be accessed directly by the mobile platform for user to manage basic operations
  that requires identification

