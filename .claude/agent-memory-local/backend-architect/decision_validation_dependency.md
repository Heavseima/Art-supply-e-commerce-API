---
name: validation-dependency-decision
description: Status of the spring-boot-starter-validation dependency approval request for the artshop project
metadata:
  type: project
---

`spring-boot-starter-validation` was APPROVED and ADDED to pom.xml on 2026-06-06 (version managed by the Boot 4.0.6 parent BOM, no explicit pin). Build verified green with `mvnw clean compile test-compile`.

**Why:** Spring Boot no longer pulls Bean Validation in transitively via the web starter; request DTOs (`OrderRequest`, `CartValidationRequest`, `OrderLine`) and controllers use `jakarta.validation.*` annotations.

**How to apply:** Decision is CLOSED/APPROVED. Validation is now on the classpath — `@Valid` and jakarta.validation constraints are usable in controllers/DTOs. No further action needed for this dependency.
