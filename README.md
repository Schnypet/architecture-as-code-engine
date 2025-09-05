# Architecture as Code Engine

The Architecture Engine implements the architecture metamodel and serves as the Single Source of Truth for all architecture information. This HSLU project provides a comprehensive platform for managing, validating, and accessing architecture models using PKL configuration files.

## Core Features

- **Central Model Management**: Complete management of architecture models across business, application, and technology layers
- **Model Validation**: Comprehensive validation of architecture models for consistency and correctness
- **Merge Logic**: Integration of manually created and automatically detected architecture elements
- **REST API**: Comprehensive RESTful API for architecture model management and querying
- **PKL Integration**: Type-safe configuration management using PKL (Pkl Configuration Language)
- **OpenAPI Documentation**: Full API documentation with Swagger UI

## Technical Implementation

- **Language**: Kotlin 2.2.0 targeting JVM 17
- **Framework**: Spring Boot 3.2.1
- **Web Framework**: Spring MVC with validation support
- **Build Tool**: Maven with Kotlin integration
- **Configuration**: PKL 0.26.3 with Spring Boot integration
- **API Documentation**: SpringDoc OpenAPI 2.2.0 with Swagger UI
- **Monitoring**: Spring Boot Actuator
- **Test Framework**: JUnit 5 + Kotlin Test + Spring Boot Test
- **Architecture**: Clean Architecture with hexagonal structure

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
- **API Documentation**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## REST API Documentation

The engine provides a comprehensive REST API organized around the architecture metamodel with four main controllers:

### Architecture Management (`/api/v1/architectures`)

- `GET /api/v1/architectures` - Get all architecture models
- `GET /api/v1/architectures/{id}` - Get specific architecture by ID
- `POST /api/v1/architectures` - Create new architecture model
- `PUT /api/v1/architectures/{id}` - Update existing architecture
- `DELETE /api/v1/architectures/{id}` - Delete architecture model
- `POST /api/v1/architectures/{id}/validate` - Validate architecture model
- `POST /api/v1/architectures/reload` - Reload models from PKL files

### Business Layer API (`/api/v1/architectures/{architectureId}/business`)

- `GET /` - Get complete business layer
- `GET /domains` - Get business domains
- `GET /capabilities` - Get business capabilities  
- `GET /actors` - Get business actors
- `GET /processes` - Get business processes
- `GET /services` - Get business services
- `GET /actors/{actorId}` - Get specific business actor
- `GET /actors/by-type/{actorType}` - Filter actors by type (INTERNAL, EXTERNAL, PARTNER)

### Application Layer API (`/api/v1/architectures/{architectureId}/application`)

- `GET /` - Get complete application layer
- `GET /applications` - Get all applications
- `GET /components` - Get application components
- `GET /services` - Get application services
- `GET /interfaces` - Get application interfaces
- `GET /applications/{applicationId}` - Get specific application
- `GET /applications/by-stereotype/{stereotype}` - Filter by stereotype
- `GET /applications/by-lifecycle/{lifecycle}` - Filter by lifecycle stage
- `GET /components/by-type/{componentType}` - Filter components by type

### Technology Layer API (`/api/v1/architectures/{architectureId}/technology`)

- `GET /` - Get complete technology layer
- `GET /nodes` - Get technology nodes
- `GET /services` - Get technology services
- `GET /artifacts` - Get artifacts
- `GET /interfaces` - Get technology interfaces
- `GET /system-software` - Get system software
- `GET /nodes/by-type/{nodeType}` - Filter nodes by type
- `GET /nodes/by-location/{location}` - Filter nodes by location

## Domain Model

The architecture engine implements a comprehensive metamodel organized in three layers:

### Architecture Structure
- **Architecture**: Root container with uid, name, description, version, and metadata
- **BusinessLayer**: Contains business domain elements
- **ApplicationLayer**: Contains application portfolio elements  
- **TechnologyLayer**: Contains technology infrastructure elements
- **Relationships**: Connections between architecture elements across layers

### Business Layer Elements
- **BusinessDomain**: Strategic business areas
- **BusinessCapability**: Business capabilities with hierarchical levels
- **BusinessActor**: Internal/external/partner actors
- **BusinessProcess**: Core/support/management processes with inputs/outputs
- **BusinessService**: Business services with SLA information

### Application Layer Elements
- **Application**: Applications with stereotypes (BUSINESS_APPLICATION, IT_APPLICATION, PLATFORM, etc.) and lifecycle stages
- **ApplicationComponent**: Components by type (FRONTEND, BACKEND, DATABASE, etc.)
- **ApplicationService**: Application services and APIs
- **ApplicationInterface**: Interfaces by type (API, UI, FILE, MESSAGE)

### Technology Layer Elements
- **TechnologyNode**: Infrastructure nodes (servers, devices, networks) with location and capacity
- **TechnologyService**: Technology services and platforms
- **Artifact**: Deployable artifacts and configurations
- **TechnologyInterface**: Technology interfaces and protocols
- **SystemSoftware**: Operating systems, middleware, databases

All elements use consistent `uid` (unique identifiers) instead of traditional IDs, with optional metadata, documentation, and properties for extensibility.

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

## Current Implementation Status

The project has evolved significantly beyond initial setup and includes:

**✅ Completed Features:**
- Complete domain model implementation with business, application, and technology layers
- REST API with comprehensive CRUD operations for all architecture elements
- PKL configuration integration with Spring Boot
- OpenAPI/Swagger documentation and UI
- Model validation framework
- Clean architecture structure with hexagonal pattern
- Comprehensive test coverage structure

**🚧 In Progress:**
- Architecture model validation rules
- PKL file loading and merge logic
- Advanced filtering and querying capabilities

**📋 Planned Features:**
- WebSocket support for real-time architecture updates
- Advanced merge engine for automatic detection integration
- Multi-tenancy support
- Architecture visualization endpoints
- Export capabilities (various formats)

## Architecture Implementation

The system follows Clean Architecture principles with clear separation:

1. **Domain Layer** (`domain/model`, `domain/service`): Core business logic and entities
2. **Application Layer** (`application/service`): Use cases and application services
3. **Infrastructure Layer** (`infrastructure/adapter`): External concerns (REST, PKL, persistence)
4. **Configuration** (`infrastructure/config`): Spring Boot and OpenAPI configuration

## Testing

The project uses a comprehensive testing approach:

### Test Framework Stack
- **JUnit 5**: Core testing framework
- **Kotlin Test**: Kotlin-specific test utilities
- **Spring Boot Test**: Integration testing with Spring context
- **Spring Boot Test Slices**: Focused testing (@WebMvcTest, @JsonTest, etc.)

### Test Structure
- **Unit Tests**: Domain model and business logic testing
- **Integration Tests**: API endpoint and service integration testing
- **Repository Tests**: Data persistence layer testing
- **PKL Integration Tests**: Configuration loading and mapping tests

### Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=DomainModelTest

# Run tests for specific package
mvn test -Dtest="tv.schnyder.aac.domain.**"
```

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use data classes for domain models
- Implement Clean Architecture patterns
- Apply SOLID principles
- Use meaningful naming conventions

### PKL Configuration
- Place PKL files in `src/main/resources/pkl/` directory
- Use type-safe PKL schemas for configuration
- Generate Kotlin classes from PKL when needed
- Validate PKL configurations at compile time

### API Development
- Document all endpoints with OpenAPI annotations
- Use proper HTTP status codes
- Implement consistent error handling
- Follow RESTful API principles
- Include request/response validation

### Contributing
1. Create feature branches from `main`
2. Write comprehensive tests for new features
3. Update API documentation
4. Follow existing code patterns and structure
5. Ensure all tests pass before merging

### Useful Commands
```bash
# Start application with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Generate PKL classes (when needed)
mvn compile

# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dlogging.level.tv.schnyder.aac=DEBUG"

# Clean and rebuild
mvn clean compile
```
