## 🛠️ Build & Runtime Commands
- **Compile & Validate:** `./mvnw clean compile`
- **Boot Application Engine:** `./mvnw spring-boot:run`
- **Execute Verification Suite:** `./mvnw test`

## 📁 Repository Directory Structure
```text
├── src/main/java/com/artshop/
│   ├── controller/      # REST Endpoints (ResponseEntity wrapped)
│   ├── service/         # Pure business logic interfaces & implementations
│   ├── repository/      # MyBatis @Mapper interface definitions
│   ├── model/
│   │   ├── entity/      # Database table mappings (Mutable)
│   │   └── dto/         # API contract data records (Immutable)
│   └── exception/       # Global @RestControllerAdvice & RFC 7807 handlers
└── src/main/resources/
    ├── mapper/          # MyBatis XML SQL query mapping files
    └── schema.sql       # Local core PostgreSQL table structure