# Hexagonal Credit Approval Service

A robust microservice implementing **Hexagonal Architecture (Ports and Adapters)** for credit analysis and approval. This project demonstrates clean architecture principles with strict dependency rules enforced by ArchUnit tests.

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Architecture Rules](#architecture-rules)
- [Package Structure](#package-structure)
- [Layer Dependencies](#layer-dependencies)
- [ArchUnit Tests](#archunit-tests)
- [How It Works](#how-it-works)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Kafka Topics](#kafka-topics)

---

## 🎯 Overview

This service processes credit analysis requests by:
1. Receiving credit analysis requests via Kafka
2. Checking for recent credit analyses (last 30 days)
3. Fetching customer credit scores from an external service
4. Applying business rules to approve or deny credit
5. Calculating approved credit limits based on score and age
6. Persisting results in MongoDB
7. Notifying results via Kafka
8. Providing REST API to query credit analysis by customer

The architecture ensures **complete separation of concerns** where the business logic (Domain) is isolated from external concerns (databases, APIs, message brokers).

---

## 🛠 Technology Stack

- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Cloud 2025.1.0**
- **MongoDB** (Database)
- **Apache Kafka** (Message Broker)
- **MapStruct 1.6.3** (Object Mapping)
- **Lombok** (Code Generation)
- **ArchUnit 1.4.1** (Architecture Testing)
- **Spring Data MongoDB**
- **Spring Kafka**
- **OpenFeign** (HTTP Client)

---

## 🏗 Architecture Rules

This project implements **strict dependency rules** that MUST be followed. These rules are enforced by ArchUnit tests in `HexagonalArchitectureTest.java`.

### The Golden Rules

| Layer | Can Import | Cannot Import | Purpose |
|-------|-----------|---------------|---------|
| **Config** | EVERYTHING | - | Wires everything together by creating Spring Beans that link Adapters to UseCases |
| **Adapters** | Ports, Domain | UseCases, Config, other Adapters | Implement Ports (output) or call Ports (input). Convert data using Mappers with Domain |
| **Ports** | Domain ONLY | Adapters, Config, UseCases | Define contracts/interfaces. Only know about Domain entities |
| **UseCases** | Ports, Domain | Adapters, Config | Contain business logic. Orchestrate Port calls. Implement Input Ports |
| **Domain** | Java ONLY | Ports, Adapters, Config, UseCases | Pure business logic. No external dependencies |

### Visual Dependency Flow

```
┌─────────────────────────────────────────────────────────────┐
│                         Config                               │
│  (Imports EVERYTHING - creates Beans linking Adapters→UseCases)│
└─────────────────────────────────────────────────────────────┘
                              ▲
                              │
         ┌────────────────────┼────────────────────┐
         │                    │                    │
┌────────┴────────┐  ┌────────┴────────┐  ┌───────┴────────┐
│   Adapters In   │  │   Adapters Out  │  │    UseCases    │
│ (Controller,    │  │ (Repository,    │  │  (Business     │
│  Consumer)      │  │  External API)  │  │   Logic)       │
└────────┬────────┘  └────────┬────────┘  └───────┬────────┘
         │                    │                    │
         │                    │                    │
         │        ┌───────────┴───────────┐        │
         │        │                       │        │
┌────────┴────────┴────────┐    ┌────────┴────────┴────────┐
│      Ports In            │    │      Ports Out           │
│  (Input interfaces)      │    │  (Output interfaces)     │
└────────┬─────────────────┘    └────────┬─────────────────┘
         │                                │
         └────────────┬───────────────────┘
                      │
         ┌────────────┴────────────┐
         │                         │
         │      Domain             │
         │  (Business Entities)    │
         │  (Imports ONLY Java)    │
         └─────────────────────────┘
```

### Key Principles

1. **Domain is the Core**: The Domain layer is the heart of the application. It contains only business logic and knows nothing about the outside world.

2. **Ports are Contracts**: Ports define the interfaces that the UseCases need. They are the only way UseCases communicate with the outside world.

3. **Adapters are Implementations**: Adapters implement the Ports. They handle all external concerns (HTTP, Database, Kafka, etc.).

4. **Config is the Glue**: The Config package is the ONLY place that knows about everything. It creates Spring Beans to wire Adapters into UseCases.

5. **No Circular Dependencies**: Adapters In cannot depend on Adapters Out, and vice versa. They only know about Ports and Domain.

---

## 📁 Package Structure

```
com.joao.hexagonal_credit_approval_service
├── adapter/
│   ├── in/                          # Driving Adapters (External → Internal)
│   │   ├── controller/              # REST Controllers
│   │   │   └── CreditAnalysisController.java
│   │   ├── consumer/                # Kafka Consumers
│   │   │   └── ReceiveCreditAnalysisConsumer.java
│   │   ├── dto/                     # Request/Response DTOs
│   │   │   └── response/
│   │   │       └── CreditAnalysisByCustomerIdResponseDTO.java
│   │   ├── mapper/                  # Mappers (DTO ↔ Domain)
│   │   │   └── CreditAnalysisMapper.java
│   │   ├── message/                 # Kafka Messages
│   │   │   └── CreditAnalysisMessage.java
│   │   └── handler/                 # Exception Handlers
│   │       ├── GlobalExceptionHandler.java
│   │       └── ApiError.java
│   ├── out/                         # Driven Adapters (Internal → External)
│   │   ├── CheckRecentCreditAnalysisAdapter.java
│   │   ├── GetAllAnalysisByCustomerOutputPortAdapter.java
│   │   ├── GetClientScoreAdapter.java
│   │   ├── NotifyCreditAnalysisAdapter.java
│   │   ├── SaveCreditAnalysisAdapter.java
│   │   ├── client/                  # External API Clients
│   │   │   └── SerasaScoreFakeApiClient.java
│   │   ├── entity/                  # Database Entities
│   │   │   └── CreditAnalysisEntity.java
│   │   ├── mapper/                  # Mappers (Entity ↔ Domain)
│   │   │   ├── CreditAnalysisEntityMapper.java
│   │   │   └── CreditAnalysisResultMessageMapper.java
│   │   ├── message/                 # Kafka Messages
│   │   │   └── CreditAnalysisResultMessage.java
│   │   └── repository/              # Spring Data Repositories
│   │       └── CreditAnalysisRepository.java
│   └── exception/                   # Custom Exceptions
│       ├── NotFoundException.java
│       └── SerializeException.java
├── application/
│   ├── core/
│   │   ├── domain/                  # DOMAIN LAYER (Pure Business Logic)
│   │   │   ├── CreditAnalysis.java  # Main business entity
│   │   │   └── Status.java          # Enum: APPROVED, DENIED
│   │   └── useCase/                 # USECASE LAYER (Business Orchestration)
│   │       ├── ValidateCreditAnalysisUseCase.java
│   │       └── GetAllCreditAnalysisByCustomerUseCase.java
│   └── ports/
│       ├── in/                      # Input Ports (Interfaces for UseCases)
│       │   ├── ValidateCreditAnalysisInputPort.java
│       │   └── GetAllCreditAnalysisByCustomerInputPort.java
│       └── out/                     # Output Ports (Interfaces for Adapters)
│           ├── CheckRecentCreditAnalysisOutputPort.java
│           ├── GetAllAnalysisByCustomerOutputPort.java
│           ├── GetClientScoreOutputPort.java
│           ├── NotifyCreditAnalysisOutputPort.java
│           └── SaveCreditAnalysisOutputPort.java
└── config/                          # CONFIG LAYER (Wiring)
    ├── GetAllCreditAnalysisByCustomerConfig.java
    ├── KafkaConsumerConfig.java
    ├── KafkaProducerConfig.java
    └── ValidateCreditAnalysisConfig.java
```

---

## 🔗 Layer Dependencies

### Domain Layer (`application.core.domain`)

**Imports**: ONLY Java standard library (`java.math.BigDecimal`, `java.time.LocalDateTime`, `java.util.UUID`)

**Purpose**: Contains pure business logic and domain entities.

**Example** - `CreditAnalysis.java`:
```java
package com.joao.hexagonal_credit_approval_service.application.core.domain;

import java.math.BigDecimal;  // ✅ Java standard library
import java.time.LocalDateTime;  // ✅ Java standard library
import java.util.UUID;  // ✅ Java standard library

public class CreditAnalysis {
    // Business logic method
    public void validate(boolean hasRecentCreditAnalysis, Long score) {
        // Pure business rules - no external dependencies
    }
}
```

### Ports Layer (`application.ports`)

**Imports**: Domain ONLY (and Java standard library)

**Purpose**: Define contracts/interfaces that UseCases depend on.

**Example** - Input Port:
```java
package com.joao.hexagonal_credit_approval_service.application.ports.in;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain

public interface ValidateCreditAnalysisInputPort {
    CreditAnalysis validate(CreditAnalysis creditAnalysis);
}
```

**Example** - Output Port:
```java
package com.joao.hexagonal_credit_approval_service.application.ports.out;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain

public interface SaveCreditAnalysisOutputPort {
    void save(CreditAnalysis creditAnalysis);
}
```

### UseCase Layer (`application.core.useCase`)

**Imports**: Ports (in and out) + Domain

**Purpose**: Orchestrate business logic by calling multiple Ports.

**Example** - `ValidateCreditAnalysisUseCase.java`:
```java
package com.joao.hexagonal_credit_approval_service.application.core.useCase;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain
import com.joao.hexagonal_credit_approval_service.application.ports.in.ValidateCreditAnalysisInputPort;  // ✅ Port
import com.joao.hexagonal_credit_approval_service.application.ports.out.GetClientScoreOutputPort;  // ✅ Port
import com.joao.hexagonal_credit_approval_service.application.ports.out.CheckRecentCreditAnalysisOutputPort;  // ✅ Port
import com.joao.hexagonal_credit_approval_service.application.ports.out.SaveCreditAnalysisOutputPort;  // ✅ Port
import com.joao.hexagonal_credit_approval_service.application.ports.out.NotifyCreditAnalysisOutputPort;  // ✅ Port

public class ValidateCreditAnalysisUseCase implements ValidateCreditAnalysisInputPort {
    // Constructor injection of Output Ports
    public ValidateCreditAnalysisUseCase(
        CheckRecentCreditAnalysisOutputPort checkRecentCreditAnalysisOutputPort,
        GetClientScoreOutputPort getClientScoreOutputPort,
        SaveCreditAnalysisOutputPort saveCreditAnalysisOutputPort,
        NotifyCreditAnalysisOutputPort notifyCreditAnalysisOutputPort
    ) { ... }

    @Override
    public CreditAnalysis validate(CreditAnalysis creditAnalysis) {
        // Orchestrate business logic using Ports
        boolean hasRecent = checkRecentCreditAnalysisOutputPort.hasRecent(creditAnalysis.getCustomerId());
        Long score = getClientScoreOutputPort.getScore(creditAnalysis.getCustomerId());
        creditAnalysis.validate(hasRecent, score);  // Domain business logic
        saveCreditAnalysisOutputPort.save(creditAnalysis);
        notifyCreditAnalysisOutputPort.send(creditAnalysis);
        return creditAnalysis;
    }
}
```

### Adapters Layer (`adapter`)

**Imports**: Ports (to implement or call) + Domain (for data conversion in Mappers)

**Purpose**: Implement Ports (output adapters) or call Ports (input adapters). Handle external concerns.

**Example** - Output Adapter (implements a Port):
```java
package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.entity.CreditAnalysisEntity;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.adapter.out.mapper.CreditAnalysisEntityMapper;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.adapter.out.repository.CreditAnalysisRepository;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain
import com.joao.hexagonal_credit_approval_service.application.ports.out.SaveCreditAnalysisOutputPort;  // ✅ Port

@Service
public class SaveCreditAnalysisAdapter implements SaveCreditAnalysisOutputPort {
    @Override
    public void save(CreditAnalysis creditAnalysis) {
        // Convert Domain to Entity using Mapper
        CreditAnalysisEntity entity = creditAnalysisMapper.toCreditAnalysisEntity(creditAnalysis);
        creditAnalysisRepository.save(entity);  // External concern (MongoDB)
    }
}
```

**Example** - Input Adapter (calls a Port):
```java
package com.joao.hexagonal_credit_approval_service.adapter.in.controller;

import com.joao.hexagonal_credit_approval_service.adapter.in.dto.response.CreditAnalysisByCustomerIdResponseDTO;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.adapter.in.mapper.CreditAnalysisMapper;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain
import com.joao.hexagonal_credit_approval_service.application.ports.in.GetAllCreditAnalysisByCustomerInputPort;  // ✅ Port

@RestController
public class CreditAnalysisController {
    private final GetAllCreditAnalysisByCustomerInputPort inputPort;  // ✅ Port

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CreditAnalysisByCustomerIdResponseDTO>> getAll(@PathVariable UUID customerId) {
        List<CreditAnalysis> creditAnalysisList = inputPort.getAll(customerId);  // Call Port
        // Convert Domain to DTO using Mapper
        List<CreditAnalysisByCustomerIdResponseDTO> response = creditAnalysisList.stream()
            .map(creditAnalysisMapper::toCreditAnalysisByCustomerIdResponseDTO)
            .toList();
        return ResponseEntity.ok(response);
    }
}
```

**Example** - Mapper (converts between Domain and DTO/Entity):
```java
package com.joao.hexagonal_credit_approval_service.adapter.in.mapper;

import com.joao.hexagonal_credit_approval_service.adapter.in.dto.response.CreditAnalysisByCustomerIdResponseDTO;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.adapter.in.message.CreditAnalysisMessage;  // ✅ Internal to adapter
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;  // ✅ Domain

@Mapper(componentModel = "spring")
public interface CreditAnalysisMapper {
    CreditAnalysis toCreditAnalysis(CreditAnalysisMessage message);  // Message → Domain
    CreditAnalysisByCustomerIdResponseDTO toCreditAnalysisByCustomerIdResponseDTO(CreditAnalysis creditAnalysis);  // Domain → DTO
}
```

### Config Layer (`config`)

**Imports**: EVERYTHING (Adapters, UseCases, Ports, Domain)

**Purpose**: Create Spring Beans to wire Adapters into UseCases. This is the ONLY place that knows about both Adapters and UseCases.

**Example** - `ValidateCreditAnalysisConfig.java`:
```java
package com.joao.hexagonal_credit_approval_service.config;

import com.joao.hexagonal_credit_approval_service.adapter.out.CheckRecentCreditAnalysisAdapter;  // ✅ Adapter
import com.joao.hexagonal_credit_approval_service.adapter.out.GetClientScoreAdapter;  // ✅ Adapter
import com.joao.hexagonal_credit_approval_service.adapter.out.NotifyCreditAnalysisAdapter;  // ✅ Adapter
import com.joao.hexagonal_credit_approval_service.adapter.out.SaveCreditAnalysisAdapter;  // ✅ Adapter
import com.joao.hexagonal_credit_approval_service.application.core.useCase.ValidateCreditAnalysisUseCase;  // ✅ UseCase

@Configuration
public class ValidateCreditAnalysisConfig {
    @Bean
    public ValidateCreditAnalysisUseCase creditAnalysisUseCase(
        CheckRecentCreditAnalysisAdapter checkRecentCreditAnalysisAdapter,  // Inject Adapter
        GetClientScoreAdapter getClientScoreAdapter,  // Inject Adapter
        SaveCreditAnalysisAdapter saveCreditAnalysisAdapter,  // Inject Adapter
        NotifyCreditAnalysisAdapter notifyCreditAnalysisAdapter  // Inject Adapter
    ) {
        return new ValidateCreditAnalysisUseCase(
            checkRecentCreditAnalysisAdapter,
            getClientScoreAdapter,
            saveCreditAnalysisAdapter,
            notifyCreditAnalysisAdapter
        );  // Wire Adapters into UseCase
    }
}
```

---

## 🧪 ArchUnit Tests

The project includes comprehensive ArchUnit tests in `HexagonalArchitectureTest.java` to enforce the architecture rules. These tests ensure that the dependency rules are never violated.

### Test 1: Layered Architecture (`shouldRespectHexagonalLayers`)

This test defines the layers and their allowed dependencies:

```java
layeredArchitecture()
    .layer("Domain").definedBy("...application.core.domain..")
    .layer("UseCase").definedBy("...application.core.useCase..")
    .layer("PortsIn").definedBy("...application.ports.in..")
    .layer("PortsOut").definedBy("...application.ports.out..")
    .layer("AdaptersIn").definedBy("...adapter.in..")
    .layer("AdaptersOut").definedBy("...adapter.out..")
    .layer("Config").definedBy("...config..")

    // Domain can be accessed by everyone (it's the core)
    .whereLayer("Domain").mayOnlyBeAccessedByLayers(
        "UseCase", "PortsIn", "PortsOut", "AdaptersIn", "AdaptersOut", "Config"
    )

    // UseCase can ONLY be accessed by Config
    .whereLayer("UseCase").mayOnlyBeAccessedByLayers("Config")

    // PortsIn can be accessed by UseCase (implements), AdaptersIn (calls), and Config
    .whereLayer("PortsIn").mayOnlyBeAccessedByLayers("UseCase", "AdaptersIn", "Config")

    // PortsOut can be accessed by UseCase (calls), AdaptersOut (implements), and Config
    .whereLayer("PortsOut").mayOnlyBeAccessedByLayers("UseCase", "AdaptersOut", "Config")

    // Adapters can ONLY be accessed by Config
    .whereLayer("AdaptersIn").mayOnlyBeAccessedByLayers("Config")
    .whereLayer("AdaptersOut").mayOnlyBeAccessedByLayers("Config")

    // Config cannot be accessed by anyone
    .whereLayer("Config").mayNotBeAccessedByAnyLayer();
```

**What this enforces**:
- ✅ Domain is accessible to all layers (everyone depends on Domain)
- ✅ UseCase is only accessible to Config (no layer directly depends on UseCase except Config)
- ✅ Ports are accessible to UseCase, their corresponding Adapters, and Config
- ✅ Adapters are only accessible to Config (no layer directly depends on Adapters except Config)
- ✅ Config is not accessible to any layer (Config is the top-level wiring layer)

### Test 2: Domain and UseCases Should Not Depend on Adapters (`domainAndUseCasesShouldNotDependOnAdapters`)

```java
noClasses()
    .that().resideInAPackage("...application.core..")
    .should().dependOnClassesThat().resideInAnyPackage(
        "...adapter..",
        "...config..");
```

**What this enforces**:
- ❌ Domain and UseCases CANNOT import anything from `adapter` package
- ❌ Domain and UseCases CANNOT import anything from `config` package
- ✅ This ensures the business logic is completely isolated from external concerns

### Test 3: Domain and UseCases Should Not Depend on Frameworks (`domainAndUseCasesShouldNotDependOnFrameworks`)

```java
noClasses()
    .that().resideInAPackage("...application.core..")
    .should().dependOnClassesThat().resideInAnyPackage(
        "org.springframework..",
        "org.apache.kafka..",
        "org.springframework.kafka..",
        "jakarta.persistence..",
        "org.springframework.data..");
```

**What this enforces**:
- ❌ Domain and UseCases CANNOT depend on Spring, Kafka, JPA, or any framework
- ✅ This ensures the business logic is pure and framework-agnostic
- ✅ Makes the code testable and portable

### Test 4: Ports Should Only Depend on Domain (`portsShouldOnlyDependOnDomain`)

```java
noClasses()
    .that().resideInAPackage("...application.ports..")
    .should().dependOnClassesThat().resideInAnyPackage(
        "...adapter..",
        "...config..",
        "...application.core.useCase..");
```

**What this enforces**:
- ❌ Ports CANNOT import anything from `adapter` package
- ❌ Ports CANNOT import anything from `config` package
- ❌ Ports CANNOT import anything from `useCase` package
- ✅ Ports can ONLY import Domain (and Java standard library)
- ✅ This ensures Ports remain pure contracts

### Test 5: Adapters Should Not Depend on Each Other (`adaptersShouldNotDependOnEachOther`)

```java
noClasses()
    .that().resideInAPackage("...adapter.in..")
    .should().dependOnClassesThat().resideInAPackage("...adapter.out..");
```

**What this enforces**:
- ❌ Input Adapters CANNOT depend on Output Adapters
- ✅ Adapters are completely isolated from each other
- ✅ They only communicate through Ports and Domain

---

## 🔄 How It Works

### Flow 1: Credit Analysis via Kafka

```
1. Kafka Message (CreditAnalysisMessage)
   ↓
2. ReceiveCreditAnalysisConsumer (Adapter In)
   - Uses CreditAnalysisMapper to convert Message → Domain
   ↓
3. ValidateCreditAnalysisInputPort (Port In)
   ↓
4. ValidateCreditAnalysisUseCase (UseCase)
   - Calls CheckRecentCreditAnalysisOutputPort
   - Calls GetClientScoreOutputPort
   - Calls CreditAnalysis.validate() (Domain business logic)
   - Calls SaveCreditAnalysisOutputPort
   - Calls NotifyCreditAnalysisOutputPort
   ↓
5. Output Ports implemented by Adapters Out:
   - CheckRecentCreditAnalysisAdapter → MongoDB
   - GetClientScoreAdapter → External API
   - SaveCreditAnalysisAdapter → MongoDB
   - NotifyCreditAnalysisAdapter → Kafka
```

### Flow 2: Query Credit Analysis via REST API

```
1. HTTP GET /v1/credit-analysis/customer/{customerId}
   ↓
2. CreditAnalysisController (Adapter In)
   - Calls GetAllCreditAnalysisByCustomerInputPort
   ↓
3. GetAllCreditAnalysisByCustomerInputPort (Port In)
   ↓
4. GetAllCreditAnalysisByCustomerUseCase (UseCase)
   - Calls GetAllAnalysisByCustomerOutputPort
   ↓
5. GetAllAnalysisByCustomerOutputPortAdapter (Adapter Out)
   - Queries MongoDB via CreditAnalysisRepository
   - Uses CreditAnalysisEntityMapper to convert Entity → Domain
   ↓
6. UseCase returns List<CreditAnalysis>
   ↓
7. Controller uses CreditAnalysisMapper to convert Domain → DTO
   ↓
8. Returns HTTP Response with DTOs
```

---

## 🚀 Running the Project

### Prerequisites

- Java 21
- Maven 3.8+
- Docker and Docker Compose

### Using Docker Compose (Recommended)

```bash
# Start all services (MongoDB, Kafka, Application)
docker-compose up -d

# View logs
docker-compose logs -f credit-approval-service

# Stop all services
docker-compose down
```

### Running Locally

1. Start MongoDB:
```bash
docker run -d -p 27017:27017 --name mongodb mongo
```

2. Start Kafka:
```bash
docker run -d -p 9092:9092 --name kafka \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@kafka:9093 \
  apache/kafka:latest
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run only architecture tests
./mvnw test -Dtest=HexagonalArchitectureTest
```

---

## 📚 API Documentation

### Get Credit Analysis by Customer ID

**Endpoint**: `GET /v1/credit-analysis/customer/{customerId}`

**Description**: Retrieves all credit analyses for a specific customer, ordered by analysis date (newest first).

**Response**:
```json
[
  {
    "analysisId": "uuid",
    "status": "APPROVED",
    "denialReason": null,
    "analysisDate": "2024-01-15T10:30:00",
    "approvedLimit": 1500.00
  }
]
```

**Status Codes**:
- `200 OK`: Credit analyses found
- `204 No Content`: No credit analyses found for customer

---

## 📨 Kafka Topics

### Input Topic

**Topic**: `credit-analysis-requested`

**Message Format**:
```json
{
  "analysisId": "uuid",
  "customerId": "uuid",
  "declaredIncome": 5000.00,
  "age": 35
}
```

### Output Topic

**Topic**: `credit-analysis-result`

**Message Format**:
```json
{
  "analysisId": "uuid",
  "customerId": "uuid",
  "status": "APPROVED",
  "approvedLimit": 1500.00
}
```

---

## 💡 Business Logic

The credit approval logic in `CreditAnalysis.validate()` follows these rules:

1. **Recent Analysis Check**: If the customer has a denied credit analysis in the last 30 days → DENY
2. **Credit Score Rules**:
   - Score 0-399 → DENY (score too low)
   - Score 400-699 → APPROVE with 30% of declared income
   - Score 700-1000 → APPROVE with 50% of declared income
3. **Age Adjustment**: If age < 21 or > 65 → Reduce approved limit by 20%
4. **Minimum Limit**: If approved limit < 500.00 → DENY

---

## 🎓 Key Takeaways for Learning Hexagonal Architecture

1. **Domain is King**: The Domain layer contains the most important code (business logic). It should never depend on anything external.

2. **Ports are Contracts**: Ports define what the UseCases need. They are the boundary between the business logic and the outside world.

3. **Adapters are Pluggable**: Adapters can be swapped without changing the business logic. For example, you could replace MongoDB with PostgreSQL by only changing the Output Adapters.

4. **Config is the Orchestrator**: The Config layer is the only place that knows how to wire everything together. It's the "glue" that makes the application work.

5. **ArchUnit is Your Guard**: Architecture tests prevent accidental violations of the dependency rules. Run them frequently!

6. **No Circular Dependencies**: The layered structure prevents circular dependencies. Dependencies always flow inward toward the Domain.

7. **Testability**: Because the business logic is isolated, you can test UseCases and Domain without any external dependencies (mock the Ports).

---

## 📝 Spring Boot 4.0 Migration Notes

This project uses Spring Boot 4.0.0, which includes important changes:

### MongoDB Configuration

The MongoDB connection properties have moved from `spring.data.mongodb.*` to `spring.mongodb.*`:

**Old (Deprecated)**:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/credit_db
      uuid-representation: standard
```

**New (Current)**:
```yaml
spring:
  mongodb:
    uri: mongodb://localhost:27017/credit_db
    representation:
      uuid: standard
```

The default UUID representation is now `STANDARD` (was `JAVA_LEGACY` in previous versions).

---

## 🤝 Contributing

When contributing to this project, ensure that:

1. All ArchUnit tests pass
2. The dependency rules are strictly followed
3. New code follows the hexagonal architecture principles
4. All tests pass before submitting a PR

---

## 📄 License

This project is for educational purposes to demonstrate Hexagonal Architecture principles.

---

## 👨‍💻 Author

Created as a learning resource for understanding Hexagonal Architecture (Ports and Adapters) in Spring Boot applications.