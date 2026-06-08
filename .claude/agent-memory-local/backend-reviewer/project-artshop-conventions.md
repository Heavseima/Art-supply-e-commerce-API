---
name: project-artshop-conventions
description: ArtShop Spring Boot project layering conventions, error handling location, DTO patterns, and known architectural decisions — updated after 2026-06-06 fix verification pass
metadata:
  type: project
---

ArtShop is a Spring Boot 4.0.6 + MyBatis + PostgreSQL e-commerce backend (base package `com.artshop`).

**Why:** First full review pass on 2026-06-06; fix-verification pass also on 2026-06-06. Captures what is confirmed fixed vs. still outstanding.

**How to apply:** Use as baseline when reviewing future PRs — flag deviations from these patterns.

## Established conventions (post-fix-pass)

- Global exception handler: `com.artshop.exception.GlobalExceptionHandler` (`@RestControllerAdvice`, RFC 7807 ProblemDetail). Now extends `ResponseEntityExceptionHandler`. Domain-exception handlers still return bare `ProblemDetail` (not `ResponseEntity<ProblemDetail>`); Spring Boot 4 serializes the embedded status correctly. Framework-exception overrides return `ResponseEntity<Object>` with proper signatures.
- DTOs: immutable Java records in `com.artshop.model.dto`. Entities (Lombok `@Data`) live in `com.artshop.model.entity` and must never be exposed through controllers.
- Mapper interface scan: `@MapperScan("com.artshop.repository")`. All SQL is in XML files under `resources/mapper/`. Zero `${}` substitution confirmed.
- DTO mapping: centralized in `com.artshop.service.support.ProductDtoMapper` — a `@Component`, not a static utility.
- Schema: `resources/schema.sql`. `spring.sql.init.mode=${SQL_INIT_MODE:never}` — defaults to never, safe for prod.
- Orders are intentionally NOT persisted (by design — validation + total computation only). Schema has only `categories` + `products` tables.
- Pagination: `PagedResponse<T>` record with `of()` factory, `Math.ceil` totalPages. ProductController clamps size 1–100, floors page at 0. SQL uses `LIMIT #{limit} OFFSET #{offset}` (parameterized only).
- `@Transactional` (org.springframework.transaction.annotation): on `OrderServiceImpl.placeOrder`, `CartServiceImpl.validate(readOnly=true)`, all three `ProductServiceImpl` methods (readOnly=true). All on public interface-overriding methods — no self-invocation issues.
- SLF4J `@Slf4j` logging: ERROR on unhandled exception (with throwable for stack trace), WARN on insufficient-stock during order, INFO on successful order placement. No sensitive data (PII, passwords) in log lines.
- `OrderLineResponse`: narrow record (productId, name, sku, price, orderedQuantity, lineTotal) — no stockQty leak through order endpoint.
- `CartValidationResponse.LineResult` still exposes `availableStock` — accepted by design (cart validation endpoint is intentionally informational).
- `ProductResponse` still exposes `stockQty` — known/accepted as LOW finding, not escalated.
- Full UUID order reference: `"ORD-" + UUID.randomUUID().toString().toUpperCase()` (no 8-char substring).
- `estimatedTotal` in cart validation sums ALL requested lines regardless of stock status.

## Remaining known gaps (accepted / low priority)

- `spring-boot-devtools` in pom as runtime/optional — safe but should not reach prod artifact.
- `springdoc-openapi-starter-webmvc-ui` 3.0.2 — redundant with springdoc auto-config (deferred LOW).
- Redundant `@MapperScan` on ArtShopApplication (MyBatis Spring Boot starter scans automatically) — benign duplicate.
- `ProductServiceImpl.findById()` issues two sequential DB calls (product then category) — no join query. Still outstanding.
- `ProductServiceImpl.findAll()` still does a full `categoryMapper.findAll()` table scan per page request to build the name lookup map.
- `CategoryServiceImpl` / `CategoryController` — unbounded `findAll()`, no pagination (deferred, category list is expected to be small).
- `CartValidationResponse.LineResult.availableStock` field exposes stock level at validation time — by design for cart UX.

## Regression checks confirmed clean (2026-06-06 fix-verification pass)

- Build: `mvnw clean compile test-compile` green (only Lombok/Unsafe JVM warnings from Java 25).
- No `${}` in any mapper XML.
- No self-invoked `@Transactional` calls.
- `PagedResponse.of()` totalPages math: `Math.ceil((double)totalElements/size)` — correct; `size==0` guard returns 0.
- Offset: `page * size` — correct for 0-based page numbering.
- `handleUnexpected` catches `Exception.class` AFTER specific handlers — Spring's `@ExceptionHandler` resolution picks the most specific match first; framework exceptions are intercepted by the `ResponseEntityExceptionHandler` overrides before falling through.
- `handleMethodArgumentNotValid` override signature matches Spring MVC's parent signature exactly.
