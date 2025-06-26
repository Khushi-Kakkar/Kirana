# Kirana Register Backend System

Kirana Register is a Spring Boot-based backend system for managing small retail (kirana) store transactions. It features secure user authentication, transaction logging, reporting, and integrates with modern enterprise-grade components like Kafka, Redis, PostgreSQL, Prometheus, Grafana, and the ELK stack.

## Table of Contents

- [Project Features](#project-features)
- [Technology Stack](#technology-stack)
- [Folder Structure](#folder-structure)
- [Getting Started](#getting-started)
- [API Testing with Postman](#api-testing-with-postman)
- [Logging with ELK Stack](#logging-with-elk-stack)
- [Kafka for Report Processing](#kafka-for-report-processing)
- [Monitoring with Prometheus and Grafana](#Monitoring-with-Prometheus-and-Grafana)
---

## Project Features

- User Signup and Login with JWT-based Authentication
- Role-based Authorization (Admin/User)
- Transaction Recording APIs
- Weekly, Monthly, and Yearly Report Generation
- Redis Caching with DAO-level Cache Invalidation
- Kafka-based asynchronous reporting
- Application metrics exposed via Prometheus
- Logging piped into ELK stack for analysis

---

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.2
- **Database**: PostgreSQL
- **Cache**: Redis
- **Message Broker**: Apache Kafka
- **Logging**: Logstash + Elasticsearch + Kibana (ELK Stack)
- **Security**: Spring Security with JWT
- **Build Tool**: Maven

---

## Folder Structure

register/
├── src/main/java/com/kirana/register/
│ ├── config/ → Security, Kafka, Redis configurations
│ ├── controller/ → REST endpoints (e.g. AuthController, TransactionController)
│ ├── model/ → Entity classes and DTOs
│ ├── repository/ → JPA repositories
│ ├── service/ → Business logic implementations
│ └── KiranaRegisterApplication.java
├── src/main/resources/
│ ├── application.properties
│ └── logback-spring.xml (configured for Logstash)
├── logstash.conf → Logstash pipeline definition
├── docker-compose.yml → Container setup for ELK, Kafka, Redis
├── pom.xml → Maven dependencies and plugins
└── kirana-register.postman_collection.json → API test cases


---

## Getting Started

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Khushi-Kakkar/Kirana.git
   cd kirana
   Configure Dependencies

2. - Navigate to project root
    - Run containers: docker compose up -d
    - Run backend: ./mvnw spring-boot:run
  
---

## API-Testing with Postman
 
 import_collection: Kirana.postman_collection.json
    usage_notes: >
      Authenticate via the login API and use the returned JWT token in the Authorization header:
      Authorization: Bearer <your-token>
    test_cases:
      - Signup & Login
      - Role-protected Transaction APIs
      - Admin-only report access
      - Rate limiting tests

---

## Logging with ELK Stack
  
  log_formatting: Configured in logback-spring.xml using logstash-logback-encoder
    logstash_input_port: 5000
    kibana_url: http://localhost:5601
    index_pattern: kirana-logs-*

---

## Kafka for Report Processing

  usage: Asynchronous report and transaction event processing
    setup:
      broker_url: localhost:9092
      components:
        - KafkaTemplate for producing
        - @KafkaListener for consuming

---
## Monitoring with Prometheus and Grafana

- Application metrics exposed via `/actuator/prometheus` using Micrometer.
- Dockerized Prometheus server configured to scrape metrics from Spring Boot app.
- Grafana dashboard (JVM Micrometer) visualizes:
  - HTTP request throughput and latency
  - JVM heap and non-heap memory usage
  - Thread pool statistics

   ### How to Run

   1. Make sure the backend app is running with `/actuator/prometheus` exposed.
   2. Start Prometheus and Grafana:
      ```bash
      docker-compose up -d



