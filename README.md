inArchitecture as Code Engine

The Architecture Engine implements the architecture metamodel and serves as the Single Source of Truth for all architecture information.

## Core Features

- **Central Model Management**: Management of the central architecture model
- **Model Validation**: Validation of architecture models for consistency and correctness
- **Merge Logic**: Integration of manually created and automatically detected architecture elements
- **REST & WebSocket Communication**: APIs for client communication

## Technical Implementation

- **Language**: Kotlin 2.2.0
- **Framework**: Spring Boot 3.2.1
- **Web Framework**: Spring MVC
- **Build Tool**: Maven
- **Configuration**: PKL (Pkl Configuration Language) with Spring integration
- **Monitoring**: Spring Boot Actuator
- **Test Framework**: JUnit 5 + Spring Boot Test
- **Target Platform**: JVM 17+

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build and Run

```bash
# Compile project
mvn compile

# Run tests
mvn test

# Start Spring Boot application
mvn spring-boot:run

# Create executable JAR
mvn package

# Run JAR directly
java -jar target/architecture-as-code-engine-1.0-SNAPSHOT.jar
```

### Available Endpoints

- **Application**: `http://localhost:8080`
- **Health Check**: `http://localhost:8080/actuator/health`
- **Actuator Info**: `http://localhost:8080/actuator/info`

## Project Structure

```
src/
├── main/
│   ├── kotlin/           # Main source code
│   └── resources/        # Resources
└── test/
    ├── kotlin/          # Test source code
    └── resources/       # Test resources
```

## PKL Configuration

The project uses [PKL](https://pkl-lang.org/) (Pkl Configuration Language) with Spring integration for type-safe configuration management. PKL provides:

- Type-safe configuration with compile-time validation
- Schema definition for architecture models
- Configuration composition and inheritance
- Spring Boot configuration properties integration
- Integration with Kotlin through generated classes

### PKL Usage

PKL files (`.pkl`) can be used to define:
- Architecture metamodel schemas
- Spring Boot application configuration
- Configuration for model validation rules
- Merge logic configurations
- API endpoint configurations

### Spring PKL Integration

The `pkl-spring` dependency provides:
- Automatic PKL configuration loading
- Spring `@ConfigurationProperties` integration
- PKL validation with Spring's validation framework

## Development Status

The project is in early development phase. Spring Boot setup, Maven configuration, project structure, and PKL Spring integration are configured.

## Planned Architecture

1. **Architecture Metamodel**: Core data structures for architecture elements
2. **Model Management Layer**: Centralized storage and access to architecture information
3. **Validation Engine**: Rules and logic for architecture validation
4. **Merge Engine**: Logic for combining manual and automatically detected elements
5. **Communication Layer**: REST and WebSocket endpoints for client interaction
