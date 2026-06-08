---
name: project-conventions
description: Authoritative package/layout/config conventions for the sample-demo (artshop) Spring Boot + MyBatis + PostgreSQL project
metadata:
  type: project
---

CLAUDE.md is the authoritative spec and mandates base package `com.artshop`. The Spring Initializr scaffold under `org.example.sampledemo` was fully deleted (2026-06-06); the app now lives entirely under `com.artshop`.

**Why:** CLAUDE.md defines the corporate layering; the scaffold package was just the Spring Initializr default and is no longer needed.

**How to apply:**
- New code goes under `com.artshop`: `controller/`, `service/` (interfaces) + `service/impl/`, `repository/` (MyBatis @Mapper interfaces), `model/entity/` (mutable Lombok), `model/dto/` (immutable records), `exception/`.
- Main class is `com.artshop.ArtShopApplication` (plain `@SpringBootApplication`, no `scanBasePackages` — component scan is automatic from this package root) + `@MapperScan("com.artshop.repository")`. Test is `com.artshop.ArtShopApplicationTests`.
- Maven artifactId stays `sample-demo` and `spring.application.name=sample-demo`; these are build/app names, not the base package.
- MyBatis XML lives in `src/main/resources/mapper/*.xml`; configured via `mybatis.mapper-locations=classpath:mapper/*.xml`.
- `mybatis.configuration.map-underscore-to-camel-case=true` is set, so snake_case columns map to camelCase fields.
- Schema bootstrap: `src/main/resources/schema.sql` run via `spring.sql.init.mode=always`.
- Stack: Spring Boot 4.0.6, Java 25, MyBatis-Spring-Boot 4.0.1, springdoc 3.0.2, Lombok. Web starter is `spring-boot-starter-webmvc` (note: not the classic `-web` artifact).
- Datasource config uses env-var placeholders (DB_URL/DB_USERNAME/DB_PASSWORD) with localhost defaults; no secrets hardcoded.
