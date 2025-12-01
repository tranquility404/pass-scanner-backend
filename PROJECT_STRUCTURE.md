# Gate Pass Verification System - Project Structure

## Directory Tree

```
gatepass-verification/
│
├── pom.xml                                    # Maven configuration
├── API_DOCUMENTATION.md                       # Complete API documentation
├── PROJECT_STRUCTURE.md                       # This file
│
├── src/
│   ├── main/
│   │   ├── java/com/hackathon/gatepass/
│   │   │   │
│   │   │   ├── GatePassApplication.java       # Main Spring Boot application
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java        # Spring Security configuration
│   │   │   │   └── MongoConfig.java           # MongoDB configuration & indexes
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── PassController.java        # REST API endpoints
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── CreatePassRequest.java     # Request DTO for creating pass
│   │   │   │   ├── VerifyRequest.java         # Request DTO for verification
│   │   │   │   ├── PassResponse.java          # Response DTO for pass data
│   │   │   │   └── ErrorResponse.java         # Error response structure
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── PassNotFoundException.java           # 404 exception
│   │   │   │   ├── PassAlreadyVerifiedException.java    # 400 exception
│   │   │   │   ├── DuplicatePassCodeException.java      # 409 exception
│   │   │   │   └── GlobalExceptionHandler.java          # @ControllerAdvice
│   │   │   │
│   │   │   ├── init/
│   │   │   │   └── DataInitializer.java       # CommandLineRunner for test data
│   │   │   │
│   │   │   ├── model/
│   │   │   │   └── Pass.java                  # MongoDB document model
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   └── PassRepository.java        # MongoDB repository interface
│   │   │   │
│   │   │   └── service/
│   │   │       └── PassService.java           # Business logic layer
│   │   │
│   │   └── resources/
│   │       └── application.properties         # Application configuration
│   │
│   └── test/
│       └── java/com/hackathon/gatepass/       # Test directory
│
└── target/                                     # Build output (generated)
```

## Component Details

### 1. Main Application
- **GatePassApplication.java**: Spring Boot entry point

### 2. Configuration Layer (`config/`)
- **SecurityConfig.java**:
  - Configures HTTP Basic Authentication
  - Defines user roles (ADMIN, STAFF, SCANNER)
  - Sets up authorization rules
  - Creates in-memory users with encrypted passwords

- **MongoConfig.java**:
  - Enables MongoDB repositories
  - Ensures indexes are created on startup
  - Configures MongoDB auditing

### 3. Controller Layer (`controller/`)
- **PassController.java**:
  - Handles HTTP requests
  - Maps endpoints to service methods
  - Validates request data
  - Returns appropriate HTTP responses
  - Enforces role-based access control

### 4. Data Transfer Objects (`dto/`)
- **CreatePassRequest.java**: Request body for creating new pass
- **VerifyRequest.java**: Request body for verify/goodies operations
- **PassResponse.java**: Standardized response for pass data
- **ErrorResponse.java**: Standardized error response format

### 5. Exception Handling (`exception/`)
- **PassNotFoundException.java**: Thrown when pass not found
- **PassAlreadyVerifiedException.java**: Thrown when already verified/goodies given
- **DuplicatePassCodeException.java**: Thrown when pass code already exists
- **GlobalExceptionHandler.java**: Centralized exception handling with @ControllerAdvice

### 6. Database Initialization (`init/`)
- **DataInitializer.java**:
  - Implements CommandLineRunner
  - Seeds database with 2 test passes on startup
  - Only runs if database is empty

### 7. Model Layer (`model/`)
- **Pass.java**:
  - MongoDB document model
  - Annotated with @Document
  - Contains indexed passCode field
  - Uses Lombok for boilerplate code

### 8. Repository Layer (`repository/`)
- **PassRepository.java**:
  - Extends MongoRepository
  - Provides CRUD operations
  - Custom query methods for passCode

### 9. Service Layer (`service/`)
- **PassService.java**:
  - Contains all business logic
  - Validates business rules
  - Transforms entities to DTOs
  - Handles timestamps and status updates
  - Throws appropriate exceptions

### 10. Configuration Files
- **application.properties**:
  - Server port configuration
  - MongoDB connection settings
  - Logging levels
  - Jackson serialization settings
  - Security defaults

- **pom.xml**:
  - Maven project configuration
  - Dependencies management
  - Build plugins
  - Spring Boot parent

## Data Flow

### Request Flow
1. **HTTP Request** → Controller Layer
2. **Controller** → Validates input & checks authorization
3. **Controller** → Calls Service Layer
4. **Service** → Applies business logic
5. **Service** → Interacts with Repository
6. **Repository** → Performs MongoDB operations
7. **Service** → Transforms entity to DTO
8. **Controller** → Returns HTTP response

### Exception Flow
1. **Exception Thrown** → Service or Repository
2. **Exception Propagates** → Controller
3. **GlobalExceptionHandler** → Catches exception
4. **Handler** → Creates ErrorResponse DTO
5. **Handler** → Returns appropriate HTTP status

## Security Architecture

### Authentication
- HTTP Basic Authentication
- In-memory user store (for testing)
- BCrypt password encoding

### Authorization
- Method-level security with @PreAuthorize
- Role-based access control (RBAC)
- Three roles: ADMIN, STAFF, SCANNER

### Role Permissions
| Endpoint                  | ADMIN | STAFF | SCANNER |
|---------------------------|-------|-------|---------|
| POST /api/passes          | ✓     | ✗     | ✗       |
| GET /api/passes/scan      | ✓     | ✓     | ✓       |
| POST /{id}/verify-entry   | ✓     | ✓     | ✗       |
| POST /{id}/give-goodies   | ✓     | ✓     | ✗       |
| GET /api/passes           | ✓     | ✗     | ✗       |
| DELETE /api/passes/{id}   | ✓     | ✗     | ✗       |

## Database Schema

### Collection: `passes`

| Field            | Type      | Description                    | Indexed |
|------------------|-----------|--------------------------------|---------|
| _id              | ObjectId  | MongoDB auto-generated ID      | Yes     |
| passCode         | String    | Unique QR code identifier      | Yes     |
| teamName         | String    | Team name                      | No      |
| members          | Array     | List of member names           | No      |
| entryVerified    | Boolean   | Entry verification status      | No      |
| goodiesGiven     | Boolean   | Goodies distribution status    | No      |
| verifiedBy       | String    | Name of verifier (nullable)    | No      |
| goodiesGivenBy   | String    | Name of staff (nullable)       | No      |
| entryVerifiedAt  | Instant   | Entry verification timestamp   | No      |
| goodiesGivenAt   | Instant   | Goodies given timestamp        | No      |
| createdAt        | Instant   | Pass creation timestamp        | No      |

### Indexes
- **passCode**: Unique index for fast lookup and duplicate prevention

## Design Patterns Used

1. **Repository Pattern**: Abstracts data access logic
2. **Service Layer Pattern**: Separates business logic from controllers
3. **DTO Pattern**: Decouples API contracts from domain models
4. **Builder Pattern**: Used in DTOs and models (via Lombok)
5. **Dependency Injection**: Constructor injection with @RequiredArgsConstructor
6. **Exception Handling**: Centralized with @ControllerAdvice

## Best Practices Implemented

1. **Separation of Concerns**: Clear layer separation
2. **Clean Code**: Lombok reduces boilerplate
3. **Validation**: Jakarta Validation annotations
4. **Security**: Role-based access control
5. **Error Handling**: Consistent error responses
6. **RESTful Design**: Proper HTTP methods and status codes
7. **Immutability**: DTOs are immutable where appropriate
8. **Indexing**: Database indexes for performance
9. **Configuration**: Externalized in application.properties
10. **Documentation**: Comprehensive API documentation

## Testing Credentials

| Username | Password    | Role    |
|----------|-------------|---------|
| admin    | admin123    | ADMIN   |
| staff    | staff123    | STAFF   |
| scanner  | scanner123  | SCANNER |

## Pre-populated Test Data

1. **Pass 1**:
   - Code: HACK2024-001
   - Team: Code Warriors
   - Members: Alice Johnson, Bob Smith, Charlie Davis

2. **Pass 2**:
   - Code: HACK2024-002
   - Team: Tech Innovators
   - Members: Diana Prince, Eve Martin, Frank Castle, Grace Hopper
