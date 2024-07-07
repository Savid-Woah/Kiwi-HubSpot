# Kiwi - HubSpot - POC 🥝

🕸️ Springboot | HubSpot | PostgreSQL | Docker | AWS | GitHub Actions | Nginx | Karate

🎥 YouTube Video: https://youtu.be/TCequsK3m1c

## Table of Contents

- [Before You Start](#before-you-start)
  - [IDE](#ide)
  - [Extensions](#plug-in)
  - [Testing](#testing)
  - [Security](#security)
  - [Swagger](#swagger)
  - [Further Optimizations](#further-optimizations)
- [Installation Guide](#getting-started)
  - [Prerequisites](#pre-requisitos)
  - [Installation](#instalacion)
    - [Backend - Local](#back-end-local)
    - [Backend - Docker](#back-end-docker)    
- [Errors and Solutions](#errores-soluciones)
    - [API](#api-errores)
    - [Docker](#docker-errores)
- [Diagrams](#diagrams)
- [Support](#support)

## Before You Start

I proceed to provide clarifications on:

### IDE

The IDE used for this project was Intellij.

### Extensions

Lombok & Karate.

### Testing

JUnit & Karate

The MCDC Coverage was not specified. However, necessary unit tests were included, using JUnit and TDD (Test Driven Development) practices.

### Security

The system features 2 authentication systems, the native one integrated by the system, and OAuth2.0 with Google and/or Facebook.

The application includes a Rate Limiting resilience system by IP for protection against cyberattacks.

### Swagger

Access the API documentation at: `/swagger-ui/index.html`

### Further Optimizations

- AES algorithm for JWT encryption (upcoming feature).
- Integration with Kubernetes for optimizing VPS resources.
- Use of WASM (Web Assembly) to reduce Docker image size.
- HubSpot as Email Provider (this couldn't be implemented since Transactional Email features are an add-on (non-free) service), in the meanwhile, Java Mail Sender was used.
    - source: `https://community.hubspot.com/t5/CMS-Development/Is-it-possible-to-use-Send-a-single-transactional-email-in-a/td-p/867967`

## Installation Guide

Below are the instructions to get the project up and running.

### Prerequisites

In case of using some other HubSpot account, keep in mind the following:

- The following columns where created within these objects in order to simplify relations between them (object name is in the left side, column name in the right side, sometimes you might have to create a new group name before creating the column).
    - `Ticket - ORDER-ID`
    - `Deal - COMPANY-ID`
    - `Contact - COMPANY-ID`
    - `Product - COMPANY-ID`
- Your app must have the following redirect url: 
    - `localhost:8080/kiwi/api/v1/hubspot-oauth/call-back`

- Your app must have the following scopes (permissions)
    - `oauth`
    - `tickets`
    - `e-commerce`
    - `transactional-email`
    - `crm.schemas.deals.read`
    - `crm.objects.deals.read`
    - `crm.objects.deals.write`
    - `crm.objects.orders.write`
    - `crm.pipelines.orders.read`
    - `crm.objects.contacts.read`
    - `crm.objects.contacts.write`
    - `crm.objects.companies.write`

Make sure you have installed the following in your local environment:
- IDE
- Java 21 JDK
- Maven v3.9.6
- Docker - (preferably desktop version: [Docker Installation Guide](https://docs.docker.com/engine/install/))
- PostgreSQL (if running the backend locally):
    - username: postgres
    - password: password
    - port: 5432
    - Disclaimer: if you have a different configuration,
      make sure to update the DATABASE_URL environment variable
      found in the project's .env.dev file and application-dev.yaml.
- Karate
    - Download JAR file: https://drive.google.com/file/d/1ae_eBnhHlqtrNP5oeHestn59_u-XW9jj/view?usp=sharing
    - Place the JAR file at the root level, inside the project folder.

### Installation - Getting Started

#### Backend: Installation - Verification

- Clone the repository: `git clone https://github.com/Savid-Woah/Kiwi-HubSpot`

- Open the project in your IDE.

- Open a command terminal within the IDE.

- Run the following commands to verify unit tests and compile the project:
    - `./mvnw clean test`
    - `./mvnw package -DskipTests`

#### Backend: Local Setup

- Inside the command prompt, run the following command => `./mvnw spring-boot:run`

- The server will run at: http://localhost:8080

#### Backend: Docker Setup

- Make sure to terminate the backend process in the console (if running locally first)
to avoid port conflicts.

- Initialize Docker or open Docker Desktop (preferably).

- In the command prompt, run the following command => `./start-dev.sh`

- The backend will start by lifting the Docker container with the server.

    Note: This process may take several minutes; we suggest checking the status
    of containers in the Docker Desktop GUI.

    ... once the container is up ...

- The server will run at: http://localhost:8080

- Note: You can stop the Docker container with the command => `./stop-dev.sh`
    after this, go to Docker Desktop and delete containers, images,
    volumes, and builds to avoid errors and cache conflicts before
    lifting it again.

### All Set!

- Start testing the API 🌠

- Karate End To End Testing
    - Make sure the application is running (wether locally or vía Docker)
    - Open your browser and go to http://localhost:8080/kiwi/api/v1/hubspot-oauth/
    - Authenticate with your HubSpot Test Account
    - Get the code from the call-back
    - Replace it at line 10 inside end_to_end.feature (karate script) 
        - `And param code = <your_code>`
    - Open a new temrinal
    - Run the following command: 
        - `java -jar karate.jar src/test/resources/features/end_to_end.feature`
    - Go to your Test Account dashboard and check the results!

### Errors and Solutions

### API

- OOPS_ERROR:
    - code: 500
    - message: 'oops-error'
    - cause: Server error
- INVALID_CREDENTIALS:
    - code: 401
    - message: 'invalid-credentials'
    - cause: Invalid or non-existent credentials
- USER_NOT_FOUND:
    - code: 404
    - message: 'user-not-found'
    - cause: User with the specified id not found
- STORE_NOT_FOUND:
    - code: 404
    - message: 'store-not-found'
    - cause: Store with the specified id not found
- STOCK_NOT_FOUND:
    - code: 404
    - message: 'stock-not-found'
    - cause: Stock with the specified id not found
- ORDER_NOT_FOUND:
    - code: 404
    - message: 'order-not-found'
    - cause: Order with the specified id not found
- CONTACT_NOT_FOUND:
    - code: 404
    - message: 'contact-not-found'
    - cause: Contact with the specified id not found
- PRODUCT_NOT_FOUND:
    - code: 404
    - message: 'product-not-found'
    - cause: Contact with the specified id not found
- CUSTOMER_NOT_FOUND:
    - code: 404
    - message: 'customer-not-found'
    - cause: Contact with the specified id not found
- NO_HUBSPOT_TOKEN_PRESENT:
    - code: 401
    - message: 'no-hubspot-token-present'
    - cause: HubSpot account hasn't been linked

### Docker

- ERROR Wsl 2:
  - This indicates that the machine trying to run Docker does not have a Linux distributor installed and/or virtualization is not enabled in its BIOS
  - This must be resolved to run Docker Desktop - src: [Docker Desktop WSL 2](https://docs.docker.com/desktop/wsl/)
- GENERIC ERROR:
  - For any other Docker integration-related error with the backend, it is recommended to clear Docker cache by running the following commands:
    - `docker container prune -f`
    - `docker image prune -a -f`
    - `docker network prune -f`
    - `docker volume prune -f`

- As a last resort, we recommend reinstalling the project to solve migration issues and corrupt files. In case I discover the migration, we can do the above, and in our console execute the commands to execute it locally so that docker starts from scratch.

## Diagrams

### Business Flow

![](https://res-console.cloudinary.com/dm0wnfozg/media_explorer_thumbnails/fcf33f21fe123f6cc87cded724e77bd4/detailed)

### Tech Flow

![](https://res-console.cloudinary.com/dm0wnfozg/media_explorer_thumbnails/2d93467c977f589a55b7265c8eafe28b/detailed)

- 🛠️ Support: savidoficial09@gmail.com - Whatsapp: +57 3225447725
