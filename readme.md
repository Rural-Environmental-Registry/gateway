# RER - Gateway

[![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud%20Gateway-4.1-green.svg)](https://spring.io/projects/spring-cloud-gateway) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/) [![Docker](https://img.shields.io/badge/Docker-24+-blue.svg)](https://www.docker.com/)

## 📑 Table of Contents

- [About the Module](#about-the-module)
- [Prerequisites](#prerequisites)
- [Installation and Execution](#installation-and-execution)
- [Service Access](#service-access)
- [Implemented Features](#implemented-features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Monitoring](#monitoring)
- [Development](#development)
- [License](#license)
- [Contribution](#contribution)

---

## About the Module

The Gateway is the unified entry point of the RER system, based on Spring Cloud Gateway. It is responsible for basic routing between different microservices with proxy configurations and CORS management.

### Main Features

- 🚚 API Gateway based on Spring Cloud Gateway  
- 🔄 Basic routing between microservices  
- 🌐 Single entry point for all APIs  
- 🔧 Optional HTTP proxy configuration  
- 🌍 CORS management  
- 🔄 Circuit Breaker support (Resilience4j)  
- 🔄 Response header rewriting  

---

## Prerequisites

- Docker version 24+  
- Docker Compose version 2.20+  
- Java 17  
- Git  

---

## Installation and Execution

### Integrated Execution

This module runs automatically as part of the main RER system. To run the full system:

```bash
./start.sh
```

### Local Development

#### Build with Gradle
```bash
./gradlew build
```

#### Build with Maven
```bash
./mvnw clean package
```

#### Docker Image Build
```bash
docker build -t car-dpg-gateway .
```

#### Standalone Execution
```bash
docker-compose up
```

---

## Service Access

The Gateway acts as a reverse proxy, routing requests to the appropriate microservices:

- **Main Gateway**: `http://localhost:8080`  
- **Main Frontend**: `http://localhost/<BASE_URL>`  
- **Authentication Frontend**: `http://localhost/<BASE_URL>/<AUTHENTICATION_FRONTEND_CONTEXT_PATH>`  
- **Keycloak Admin**: `http://localhost/<BASE_URL>/<AUTHENTICATION_BASE_KEYCLOAK_BASE_URL>`  
- **backend API**: `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>`  
- **Calculation Engine**: `http://localhost/<BASE_URL>/<CALCULATION_ENGINE_CONTEXT_PATH>`  
- **Authentication Backend**: `http://localhost/<BASE_URL>/<AUTHENTICATION_BACKEND_CONTEXT_PATH>`  

---

## Implemented Features

### Request Routing

- Path-based routing  
- URL rewriting  
- StripPrefix  

### Configured Filters

- Global CORS  
- Header Injection  
- Response Header Rewriting  
- HTTP Proxy (optional)  

---

## Technologies

- Spring Cloud Gateway  
- Spring Boot 3.x  
- Spring WebFlux (Reactive)  
- Resilience4j (Circuit Breaker)  
- Netty Server  
- Gradle/Maven  
- Docker  

---

## Project Structure

```
Gateway/
├── src/
│   └── main/
│       ├── java/
│       │   └── gateway/
│       │       ├── Application.java        # Main application
│       │       └── ProxyConfig.java        # Proxy configuration
│       └── resources/
│           └── application.yml             # Route configurations
├── gradle/                                 # Gradle wrapper
├── .mvn/                                   # Maven wrapper
├── build.gradle                            # Gradle build file
├── pom.xml                                 # Maven build file
├── docker-compose.yml                      # Local orchestration
├── Dockerfile                              # Docker image
└── makefile                                # Make commands
```

---

## Configuration

### Configured Routes

Routes are dynamically configured through environment variables:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: <AUTHENTICATION_FRONTEND_SERVICE_NAME>
          uri: http://<AUTHENTICATION_FRONTEND_SERVICE_NAME>
          predicates:
            - Path=/<BASE_URL>/<AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL>/**

        - id: <CALCULATION_ENGINE_API_SERVICE_NAME>
          uri: http://<CALCULATION_ENGINE_API_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<CALCULATION_ENGINE_API_BASE_URL>/**
          filters:
            - StripPrefix=2

        - id: keycloak
          uri: http://<AUTHENTICATION_BASE_KEYCLOAK_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<AUTHENTICATION_BASE_KEYCLOAK_BASE_URL>/**

        - id: <CORE_BACKEND_API_SERVICE_NAME>
          uri: http://<CORE_BACKEND_API_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/**

        - id: <CORE_FRONTEND_SERVICE_NAME>
          uri: http://<CORE_FRONTEND_SERVICE_NAME>
          predicates:
            - Path=/**
```

---

## Environment Variables

### Routing Contexts

- `BASE_URL`  
- `AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL`  
- `AUTHENTICATION_BASE_KEYCLOAK_BASE_URL`  
- `CORE_BACKEND_API_CONTEXT_PATH`  
- `CALCULATION_ENGINE_API_BASE_URL`  
- `AUTHENTICATION_BACKEND_KEYCLOAK_BASE_URL`  

### Target Services

- `AUTHENTICATION_FRONTEND_SERVICE_NAME`  
- `AUTHENTICATION_BASE_KEYCLOAK_SERVICE_NAME`  
- `CORE_BACKEND_API_SERVICE_NAME`  
- `CALCULATION_ENGINE_API_SERVICE_NAME`  
- `CORE_FRONTEND_SERVICE_NAME`  

### Proxy Configuration (Optional)

- `PROXY_HOST`  
- `PROXY_PORT`  

---

## Monitoring

### Logs

```bash
# View real-time logs
docker-compose logs -f gateway

# Check if Gateway is responding
curl -f http://localhost:8080
```

Note: Spring Boot Actuator is not included.

---

## Container Management

```bash
# Check status
docker-compose ps

# Stop services
docker-compose down

# Rebuild
docker-compose up --build
```

---

## Development

### Local Testing

```bash
# With Gradle
./gradlew test

# With Maven
./mvnw test
```

### Local Execution (without Docker)

```bash
# With Gradle
./gradlew bootRun

# With Maven
./mvnw spring-boot:run
```

---

## Current Limitations

- No automatic load balancing  
- No authentication or authorization  
- No rate limiting  
- No metrics or health checks  
- Static route configuration  

---

## License

This project is distributed under the [GPL-3.0](https://github.com/Rural-Environmental-Registry/core/blob/main/LICENSE).

---

## Contribution

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

By submitting a pull request or patch, you affirm that you are the author of the code and that you agree to license your contribution under the terms of the GNU General Public License v3.0 (or later) for this project. You also agree to assign the copyright of your contribution to the Ministry of Management and Innovation in Public Services (MGI), the owner of this project.

---

## Support

For technical support or project-related questions:

- **Documentation:** Check the individual READMEs for each submodule
- **Issues:** Report problems via the GitHub issue tracker
 
---

Copyright (C) 2024-2025 Ministry of Management and Innovation in Public Services (MGI), Government of Brazil.

This program was developed by Dataprev as part of a contract with the Ministry of Management and Innovation in Public Services (MGI).