# Code Generation Plan — banking-contracts

## Unit Context
- **Unit**: banking-contracts (single unit — entire library)
- **Type**: Greenfield Kotlin library
- **Package root**: `com.digitalbank.contracts`
- **Code location**: `banking-contracts/src/main/kotlin/com/digitalbank/contracts/`
- **Build files**: `banking-contracts/` (workspace root, NOT aidlc-docs/)
- **Dependencies**: `kotlinx-serialization-json:1.6.3` only — NO Spring

## Generation Sequence

### Phase A: Project Structure Setup

- [x] **Step 1** — Create `settings.gradle.kts`
  - Set `rootProject.name = "banking-contracts"`
  - Enable Gradle feature previews if needed

- [x] **Step 2** — Create `build.gradle.kts`
  - Apply plugins: `kotlin("jvm")`, `kotlin("plugin.serialization")`, `maven-publish`
  - Set group = `com.digitalbank`, version = `1.0.0`
  - Add dependency: `org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3`
  - Configure `maven-publish` with `publishToMavenLocal` task
  - Configure Kotlin JVM target (JVM 17)

- [x] **Step 3** — Create `gradle/wrapper/gradle-wrapper.properties`
  - Pin to Gradle 8.5
  - distributionUrl pointing to official Gradle distribution

### Phase B: Common Package — `com.digitalbank.contracts.common`

- [x] **Step 4** — Create `MonetaryAmount.kt`
  - `@Serializable data class MonetaryAmount(val amount: String, val currency: String)`
  - KDoc: used for all monetary values; `amount` is String for decimal precision; `currency` follows ISO 4217
  - Produced by: accounts-core-svc, payments-core-svc
  - Consumed by: banking-bff, accounts-core-svc, payments-core-svc

- [x] **Step 5** — Create `PaginatedResponse.kt`
  - `@Serializable data class PaginatedResponse<T>(val items: List<T>, val page: Int, val pageSize: Int, val totalItems: Long, val totalPages: Int)`
  - KDoc: generic wrapper for paginated API responses
  - Produced by: accounts-core-svc (account lists), payments-core-svc (payment lists)
  - Consumed by: banking-bff

- [x] **Step 6** — Create `ApiError.kt`
  - `@Serializable data class ApiError(val code: String, val message: String, val traceId: String, val timestamp: String)`
  - KDoc: standard error envelope for all API error responses
  - Produced by: all services (via GlobalExceptionHandler)
  - Consumed by: banking-bff (error propagation)

- [x] **Step 7** — Create `AuditMetadata.kt`
  - `@Serializable data class AuditMetadata(val createdAt: String, val updatedAt: String, val version: Long)`
  - KDoc: standard audit fields; version for optimistic locking
  - Produced by: accounts-core-svc, payments-core-svc (as embedded or response field)
  - Consumed by: banking-bff

### Phase C: Accounts Package — `com.digitalbank.contracts.accounts`

- [x] **Step 8** — Create `AccountType.kt`
  - `enum class AccountType { CHECKING, SAVINGS, MONEY_MARKET }`
  - KDoc: classifies the account product type
  - Produced by: accounts-core-svc
  - Consumed by: banking-bff

- [x] **Step 9** — Create `AccountStatus.kt`
  - `enum class AccountStatus { ACTIVE, DORMANT, FROZEN, CLOSED }`
  - KDoc: operational state of the account; FROZEN blocks all transactions
  - Produced by: accounts-core-svc
  - Consumed by: banking-bff, payments-core-svc (during account validation)

- [x] **Step 10** — Create `AccountSummary.kt`
  - `@Serializable data class AccountSummary(val accountId: String, val accountType: AccountType, val holderName: String, val balance: MonetaryAmount, val status: AccountStatus)`
  - KDoc: lightweight account representation for list views; hides sensitive fields
  - Produced by: accounts-core-svc (`GET /api/v1/accounts`)
  - Consumed by: banking-bff (dashboard account list)

- [x] **Step 11** — Create `AccountResponse.kt`
  - `@Serializable data class AccountResponse(val accountId: String, val accountType: AccountType, val holderName: String, val balance: MonetaryAmount, val status: AccountStatus, val currency: String, val lastTransactionDate: String?, val createdAt: String)`
  - KDoc: full account detail; `lastTransactionDate` is nullable (no transactions yet)
  - Produced by: accounts-core-svc (`GET /api/v1/accounts/{id}`, hold endpoint)
  - Consumed by: banking-bff (account detail view)

- [x] **Step 12** — Create `AccountError.kt`
  - `sealed class AccountError` with sub-types:
    - `data class NotFound(val accountId: String) : AccountError()`
    - `data class InsufficientFunds(val accountId: String, val requested: MonetaryAmount, val available: MonetaryAmount) : AccountError()`
    - `data class AccountFrozen(val accountId: String) : AccountError()`
  - KDoc: typed error domain for accounts; exhaustive matching via `when`
  - Produced by: accounts-core-svc (thrown as domain exceptions)
  - Consumed by: accounts-core-svc (GlobalExceptionHandler), payments-core-svc (account validation errors)

### Phase D: Payments Package — `com.digitalbank.contracts.payments`

- [x] **Step 13** — Create `PaymentType.kt`
  - `enum class PaymentType { INTERNAL_TRANSFER, BILL_PAYMENT, SCHEDULED }`
  - KDoc: classifies the payment operation type
  - Produced by: payments-core-svc
  - Consumed by: banking-bff

- [x] **Step 14** — Create `PaymentStatus.kt`
  - `enum class PaymentStatus { PENDING, COMPLETED, FAILED, CANCELLED }`
  - KDoc: lifecycle state of a payment; PENDING is initial, terminal states are COMPLETED/FAILED/CANCELLED
  - Produced by: payments-core-svc
  - Consumed by: banking-bff

- [x] **Step 15** — Create `PaymentRequest.kt`
  - `@Serializable data class PaymentRequest(val fromAccountId: String, val toAccountId: String, val amount: MonetaryAmount, val type: PaymentType, val reference: String?)`
  - KDoc: inbound payment initiation request; `reference` is optional customer reference
  - Consumed by: payments-core-svc (`POST /api/v1/payments`)
  - Produced by: banking-bff (forwarded from dashboard transfer)

- [x] **Step 16** — Create `PaymentResponse.kt`
  - `@Serializable data class PaymentResponse(val paymentId: String, val fromAccountId: String, val toAccountId: String, val amount: MonetaryAmount, val type: PaymentType, val status: PaymentStatus, val reference: String?, val createdAt: String)`
  - KDoc: payment record returned after initiation and for status queries
  - Produced by: payments-core-svc
  - Consumed by: banking-bff (dashboard payment history, transfer result)

- [x] **Step 17** — Create `PaymentError.kt`
  - `sealed class PaymentError` with sub-types:
    - `data class InsufficientFunds(val accountId: String, val requested: MonetaryAmount, val available: MonetaryAmount) : PaymentError()`
    - `data class InvalidAccount(val accountId: String) : PaymentError()`
    - `data class DailyLimitExceeded(val limit: MonetaryAmount, val attempted: MonetaryAmount) : PaymentError()`
  - KDoc: typed error domain for payments; exhaustive matching via `when`
  - Produced by: payments-core-svc (thrown as domain exceptions)
  - Consumed by: payments-core-svc (GlobalExceptionHandler), banking-bff (error propagation)

---

## File Inventory (17 files total)

| Step | File | Location |
|---|---|---|
| 1 | `settings.gradle.kts` | root |
| 2 | `build.gradle.kts` | root |
| 3 | `gradle/wrapper/gradle-wrapper.properties` | root |
| 4 | `MonetaryAmount.kt` | `src/main/kotlin/com/digitalbank/contracts/common/` |
| 5 | `PaginatedResponse.kt` | `src/main/kotlin/com/digitalbank/contracts/common/` |
| 6 | `ApiError.kt` | `src/main/kotlin/com/digitalbank/contracts/common/` |
| 7 | `AuditMetadata.kt` | `src/main/kotlin/com/digitalbank/contracts/common/` |
| 8 | `AccountType.kt` | `src/main/kotlin/com/digitalbank/contracts/accounts/` |
| 9 | `AccountStatus.kt` | `src/main/kotlin/com/digitalbank/contracts/accounts/` |
| 10 | `AccountSummary.kt` | `src/main/kotlin/com/digitalbank/contracts/accounts/` |
| 11 | `AccountResponse.kt` | `src/main/kotlin/com/digitalbank/contracts/accounts/` |
| 12 | `AccountError.kt` | `src/main/kotlin/com/digitalbank/contracts/accounts/` |
| 13 | `PaymentType.kt` | `src/main/kotlin/com/digitalbank/contracts/payments/` |
| 14 | `PaymentStatus.kt` | `src/main/kotlin/com/digitalbank/contracts/payments/` |
| 15 | `PaymentRequest.kt` | `src/main/kotlin/com/digitalbank/contracts/payments/` |
| 16 | `PaymentResponse.kt` | `src/main/kotlin/com/digitalbank/contracts/payments/` |
| 17 | `PaymentError.kt` | `src/main/kotlin/com/digitalbank/contracts/payments/` |

## Notes
- `gradle-wrapper.jar` and `gradlew`/`gradlew.bat` scripts will be bootstrapped by running `gradle wrapper --gradle-version 8.5` after build.gradle.kts is created, OR copied from another project. Noted in Build & Test instructions.
- No test sources needed — this is a type-only library; build success is the quality gate.
- All application code goes to workspace root. Nothing in aidlc-docs/.
