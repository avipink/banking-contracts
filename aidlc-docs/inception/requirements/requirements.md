# Requirements — banking-contracts

## Intent Analysis

| Attribute | Value |
|---|---|
| **User Request** | Create the `banking-contracts` Kotlin shared contract library for the digital banking platform |
| **Request Type** | New Project (Greenfield) |
| **Scope Estimate** | Multiple Components — 13 contract types across 3 packages (accounts, payments, common) |
| **Complexity Estimate** | Moderate — well-specified types, standard Kotlin library patterns, clean dependency scope |

---

## Functional Requirements

### FR-01: Project Setup
- Kotlin 1.9.25 with Gradle 8.x and Kotlin DSL
- `build.gradle.kts` with `maven-publish` plugin
- `settings.gradle.kts` with `rootProject.name = "banking-contracts"`
- Group: `com.digitalbank`, Version: `1.0.0`
- Single dependency: `kotlinx-serialization-json 1.6.3`
- NO Spring Framework dependencies — pure Kotlin library
- `publishToMavenLocal` task that functions correctly

### FR-02: Common Package — `com.digitalbank.contracts.common`

| Type | Kind | Fields |
|---|---|---|
| `MonetaryAmount` | `@Serializable data class` | `amount: String` (precision), `currency: String` (ISO 4217) |
| `PaginatedResponse<T>` | `@Serializable generic data class` | `items: List<T>`, `page: Int`, `pageSize: Int`, `totalItems: Long`, `totalPages: Int` |
| `ApiError` | `@Serializable data class` | `code: String`, `message: String`, `traceId: String`, `timestamp: String` |
| `AuditMetadata` | `@Serializable data class` | `createdAt: String`, `updatedAt: String`, `version: Long` |

### FR-03: Accounts Package — `com.digitalbank.contracts.accounts`

| Type | Kind | Details |
|---|---|---|
| `AccountType` | `enum` | Values: `CHECKING`, `SAVINGS`, `MONEY_MARKET` |
| `AccountStatus` | `enum` | Values: `ACTIVE`, `DORMANT`, `FROZEN`, `CLOSED` |
| `AccountSummary` | `@Serializable data class` | `accountId: String`, `accountType: AccountType`, `holderName: String`, `balance: MonetaryAmount`, `status: AccountStatus` |
| `AccountResponse` | `@Serializable data class` | Extends AccountSummary fields plus: `currency: String`, `lastTransactionDate: String?` (nullable), `createdAt: String` |
| `AccountError` | `sealed class` | Sub-types: `NotFound(accountId: String)`, `InsufficientFunds(accountId: String, requested: MonetaryAmount, available: MonetaryAmount)`, `AccountFrozen(accountId: String)` |

### FR-04: Payments Package — `com.digitalbank.contracts.payments`

| Type | Kind | Details |
|---|---|---|
| `PaymentType` | `enum` | Values: `INTERNAL_TRANSFER`, `BILL_PAYMENT`, `SCHEDULED` |
| `PaymentStatus` | `enum` | Values: `PENDING`, `COMPLETED`, `FAILED`, `CANCELLED` |
| `PaymentRequest` | `@Serializable data class` | `fromAccountId: String`, `toAccountId: String`, `amount: MonetaryAmount`, `type: PaymentType`, `reference: String?` (nullable) |
| `PaymentResponse` | `@Serializable data class` | `paymentId: String`, `fromAccountId: String`, `toAccountId: String`, `amount: MonetaryAmount`, `type: PaymentType`, `status: PaymentStatus`, `reference: String?`, `createdAt: String` |
| `PaymentError` | `sealed class` | Sub-types: `InsufficientFunds(accountId: String, requested: MonetaryAmount, available: MonetaryAmount)`, `InvalidAccount(accountId: String)`, `DailyLimitExceeded(limit: MonetaryAmount, attempted: MonetaryAmount)` |

### FR-05: Documentation
- KDoc on every public type documenting:
  - Purpose of the type
  - Which services produce this type
  - Which services consume this type

### FR-06: Build Verification
- Library must compile cleanly with `./gradlew build`
- `./gradlew publishToMavenLocal` must succeed
- Consumed by downstream services via `includeBuild("../banking-contracts")` in their Gradle builds

---

## Non-Functional Requirements

### NFR-01: Compatibility
- All types `@Serializable` via `kotlinx-serialization-json` for JSON serialization
- Enums serializable by name
- Sealed class sub-types serializable with polymorphic support

### NFR-02: API Contract Stability
- This library is the **explicit API boundary** — changes to existing types are BREAKING CHANGES
- Version must be bumped for any breaking change
- No internal service domain models — only contract types

### NFR-03: Dependency Minimalism
- Zero Spring dependencies
- Only `kotlinx-serialization-json` as a runtime dependency
- Gradle plugin dependencies: `kotlin-jvm`, `kotlinx-serialization`, `maven-publish`

### NFR-04: Type Safety
- Strict Kotlin null safety — nullable fields explicitly marked with `?`
- No `!!` operator in any generated code
- `MonetaryAmount.amount` uses `String` (not `Double`) to preserve decimal precision

---

## Technical Context
- Consumed by: `accounts-core-svc`, `payments-core-svc`, `banking-bff`
- Integration method: Gradle composite build via `includeBuild("../banking-contracts")`
- Alternative: `mavenLocal()` after `publishToMavenLocal`
- This library has zero runtime footprint beyond its type definitions
