# Gate Pass Verification System - API Documentation

## Overview
This is a production-ready backend REST API for a QR-based verification system for hackathon participants.

## Base URL
```
http://localhost:8080/api
```

## Authentication
The API uses HTTP Basic Authentication with the following test credentials:

### User Roles & Credentials

| Username | Password    | Role    | Permissions                                      |
|----------|-------------|---------|--------------------------------------------------|
| admin    | admin123    | ADMIN   | Full access - create, read, update, delete passes|
| staff    | staff123    | STAFF   | Verify entry and give goodies                    |
| scanner  | scanner123  | SCANNER | Read-only access via QR scan                     |

---

## Endpoints

### 1. Create a New Pass
**POST** `/api/passes`

**Authorization:** ADMIN only

**Request Body:**
```json
{
  "passCode": "HACK2024-003",
  "teamName": "Innovation Squad",
  "members": ["John Doe", "Jane Smith", "Mike Johnson"]
}
```

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "passCode": "HACK2024-003",
  "teamName": "Innovation Squad",
  "members": ["John Doe", "Jane Smith", "Mike Johnson"],
  "entryVerified": false,
  "goodiesGiven": false,
  "verifiedBy": null,
  "goodiesGivenBy": null,
  "entryVerifiedAt": null,
  "goodiesGivenAt": null,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/passes \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "passCode": "HACK2024-003",
    "teamName": "Innovation Squad",
    "members": ["John Doe", "Jane Smith", "Mike Johnson"]
  }'
```

---

### 2. Scan Pass by QR Code
**GET** `/api/passes/scan?code={passCode}`

**Authorization:** ADMIN, STAFF, SCANNER

**Query Parameters:**
- `code` (required): The unique pass code from QR

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "passCode": "HACK2024-001",
  "teamName": "Code Warriors",
  "members": ["Alice Johnson", "Bob Smith", "Charlie Davis"],
  "entryVerified": false,
  "goodiesGiven": false,
  "verifiedBy": null,
  "goodiesGivenBy": null,
  "entryVerifiedAt": null,
  "goodiesGivenAt": null,
  "createdAt": "2024-01-15T09:00:00Z"
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/passes/scan?code=HACK2024-001" \
  -u scanner:scanner123
```

---

### 3. Verify Entry
**POST** `/api/passes/{id}/verify-entry`

**Authorization:** ADMIN, STAFF

**Path Parameters:**
- `id` (required): The pass ID

**Request Body:**
```json
{
  "verifiedBy": "John Staff"
}
```

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "passCode": "HACK2024-001",
  "teamName": "Code Warriors",
  "members": ["Alice Johnson", "Bob Smith", "Charlie Davis"],
  "entryVerified": true,
  "goodiesGiven": false,
  "verifiedBy": "John Staff",
  "goodiesGivenBy": null,
  "entryVerifiedAt": "2024-01-15T10:45:00Z",
  "goodiesGivenAt": null,
  "createdAt": "2024-01-15T09:00:00Z"
}
```

**Error Response (Already Verified):** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Entry already verified",
  "path": "/api/passes/507f1f77bcf86cd799439011/verify-entry",
  "timestamp": "2024-01-15T10:50:00Z"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/passes/507f1f77bcf86cd799439011/verify-entry \
  -u staff:staff123 \
  -H "Content-Type: application/json" \
  -d '{"verifiedBy": "John Staff"}'
```

---

### 4. Give Goodies
**POST** `/api/passes/{id}/give-goodies`

**Authorization:** ADMIN, STAFF

**Path Parameters:**
- `id` (required): The pass ID

**Request Body:**
```json
{
  "verifiedBy": "Sarah Staff"
}
```

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "passCode": "HACK2024-001",
  "teamName": "Code Warriors",
  "members": ["Alice Johnson", "Bob Smith", "Charlie Davis"],
  "entryVerified": true,
  "goodiesGiven": true,
  "verifiedBy": "John Staff",
  "goodiesGivenBy": "Sarah Staff",
  "entryVerifiedAt": "2024-01-15T10:45:00Z",
  "goodiesGivenAt": "2024-01-15T11:00:00Z",
  "createdAt": "2024-01-15T09:00:00Z"
}
```

**Error Response (Already Given):** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Goodies already given",
  "path": "/api/passes/507f1f77bcf86cd799439011/give-goodies",
  "timestamp": "2024-01-15T11:05:00Z"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/passes/507f1f77bcf86cd799439011/give-goodies \
  -u staff:staff123 \
  -H "Content-Type: application/json" \
  -d '{"verifiedBy": "Sarah Staff"}'
```

---

### 5. List All Passes
**GET** `/api/passes`

**Authorization:** ADMIN only

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "passCode": "HACK2024-001",
    "teamName": "Code Warriors",
    "members": ["Alice Johnson", "Bob Smith", "Charlie Davis"],
    "entryVerified": true,
    "goodiesGiven": true,
    "verifiedBy": "John Staff",
    "goodiesGivenBy": "Sarah Staff",
    "entryVerifiedAt": "2024-01-15T10:45:00Z",
    "goodiesGivenAt": "2024-01-15T11:00:00Z",
    "createdAt": "2024-01-15T09:00:00Z"
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "passCode": "HACK2024-002",
    "teamName": "Tech Innovators",
    "members": ["Diana Prince", "Eve Martin", "Frank Castle", "Grace Hopper"],
    "entryVerified": false,
    "goodiesGiven": false,
    "verifiedBy": null,
    "goodiesGivenBy": null,
    "entryVerifiedAt": null,
    "goodiesGivenAt": null,
    "createdAt": "2024-01-15T09:15:00Z"
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/passes \
  -u admin:admin123
```

---

### 6. Delete Pass
**DELETE** `/api/passes/{id}`

**Authorization:** ADMIN only

**Path Parameters:**
- `id` (required): The pass ID

**Response:** `204 No Content`

**Error Response (Not Found):** `404 Not Found`
```json
{
  "status": 404,
  "message": "Pass not found with id: 507f1f77bcf86cd799439011",
  "path": "/api/passes/507f1f77bcf86cd799439011",
  "timestamp": "2024-01-15T12:00:00Z"
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/passes/507f1f77bcf86cd799439011 \
  -u admin:admin123
```

---

## Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "message": "Pass not found with code: HACK2024-999",
  "path": "/api/passes/scan",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

### 400 Bad Request (Validation Error)
```json
{
  "status": 400,
  "message": "Team name is required, Members list cannot be empty",
  "path": "/api/passes",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

### 409 Conflict (Duplicate Pass Code)
```json
{
  "status": 409,
  "message": "Pass code already exists: HACK2024-001",
  "path": "/api/passes",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/passes"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/passes"
}
```

---

## Postman Collection

### Import into Postman

1. Create a new collection named "Gate Pass Verification"
2. Set collection-level authorization:
   - Type: Basic Auth
   - Username: admin
   - Password: admin123

### Requests

#### 1. Create Pass
- **Method:** POST
- **URL:** `{{baseUrl}}/api/passes`
- **Auth:** Inherit from parent (admin)
- **Body (raw JSON):**
```json
{
  "passCode": "HACK2024-003",
  "teamName": "Innovation Squad",
  "members": ["John Doe", "Jane Smith", "Mike Johnson"]
}
```

#### 2. Scan Pass
- **Method:** GET
- **URL:** `{{baseUrl}}/api/passes/scan?code=HACK2024-001`
- **Auth:** Basic Auth (scanner/scanner123)

#### 3. Verify Entry
- **Method:** POST
- **URL:** `{{baseUrl}}/api/passes/{{passId}}/verify-entry`
- **Auth:** Basic Auth (staff/staff123)
- **Body (raw JSON):**
```json
{
  "verifiedBy": "John Staff"
}
```

#### 4. Give Goodies
- **Method:** POST
- **URL:** `{{baseUrl}}/api/passes/{{passId}}/give-goodies`
- **Auth:** Basic Auth (staff/staff123)
- **Body (raw JSON):**
```json
{
  "verifiedBy": "Sarah Staff"
}
```

#### 5. Get All Passes
- **Method:** GET
- **URL:** `{{baseUrl}}/api/passes`
- **Auth:** Inherit from parent (admin)

#### 6. Delete Pass
- **Method:** DELETE
- **URL:** `{{baseUrl}}/api/passes/{{passId}}`
- **Auth:** Inherit from parent (admin)

### Environment Variables
```
baseUrl: http://localhost:8080
passId: 507f1f77bcf86cd799439011
```

---

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ running on localhost:27017

### Steps
1. Start MongoDB:
```bash
mongod
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Application will start on `http://localhost:8080`

### Test Data
The application automatically creates 2 test passes on startup:
- **HACK2024-001** - Team: Code Warriors (3 members)
- **HACK2024-002** - Team: Tech Innovators (4 members)

---

## Project Structure

```
gatepass-verification/
├── src/
│   ├── main/
│   │   ├── java/com/hackathon/gatepass/
│   │   │   ├── config/           # Security & MongoDB configuration
│   │   │   ├── controller/       # REST API controllers
│   │   │   ├── dto/              # Request/Response DTOs
│   │   │   ├── exception/        # Custom exceptions & handler
│   │   │   ├── init/             # Database initialization
│   │   │   ├── model/            # MongoDB document models
│   │   │   ├── repository/       # MongoDB repositories
│   │   │   ├── service/          # Business logic layer
│   │   │   └── GatePassApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## Business Logic Rules

1. **Entry Verification:**
   - Entry can only be verified once
   - Attempting to verify again returns 400 Bad Request
   - Timestamp and verifier name are recorded

2. **Goodies Distribution:**
   - Goodies can only be given once
   - Attempting to give again returns 400 Bad Request
   - Timestamp and staff name are recorded

3. **Pass Code Uniqueness:**
   - Each pass code must be unique
   - Duplicate pass codes return 409 Conflict

4. **Data Validation:**
   - Team name is required
   - Members list cannot be empty
   - Pass code is required
   - Verifier name is required for verification actions

---

## Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Database:** MongoDB
- **Security:** Spring Security (Basic Auth)
- **Validation:** Jakarta Validation
- **Build Tool:** Maven
- **Libraries:** Lombok, Spring Data MongoDB

---

## Support

For issues or questions, refer to the source code or contact the development team.
