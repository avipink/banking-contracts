# banking-contracts

## Role in Product
Shared contract library defining all API-boundary types used across the platform. Not a deployable
service — consumed at build time only via Gradle composite build. Defines the canonical shapes for
all inter-service request/response DTOs, sealed error hierarchies, and shared value objects.

## Package Structure
```
com.digitalbank.contracts.common    MonetaryAmount, ApiError, PaginatedResponse<T>
com.digitalbank.contracts.accounts  AccountResponse, AccountSummary, AccountStatus, AccountType, AccountError
com.digitalbank.contracts.payments  PaymentRequest, PaymentResponse, PaymentStatus, PaymentType, PaymentError
```

## Key DTOs and Shared Types
| Type | Package | Kind |
|---|---|---|
| `MonetaryAmount` | common | `data class` — `amount: String`, `currency: String` |
| `ApiError` | common | `data class` — `code`, `message`, `traceId`, `timestamp` |
| `PaginatedResponse<T>` | common | `data class` — `items`, `page`, `pageSize`, `totalItems: Long`, `totalPages` |
| `AccountResponse` | accounts | `data class` — full account record (id, type, holder, balance, status, openedAt) |
| `AccountSummary` | accounts | `data class` — list-optimized projection (id, type, holder, balance, status, openedAt) |
| `AccountStatus` | accounts | `enum` — `ACTIVE`, `FROZEN` |
| `AccountType` | accounts | `enum` — (values defined in library) |
| `PaymentRequest` | payments | `data class` — fromAccountId, toAccountId, amount, type, reference? |
| `PaymentResponse` | payments | `data class` — paymentId, fromAccountId, toAccountId, amount, type, status, reference?, createdAt |
| `PaymentStatus` | payments | `enum` — `PENDING`, `COMPLETED` |
| `PaymentType` | payments | `enum` — `INTERNAL_TRANSFER`, `BILL_PAYMENT` |

## Error Types
| Type | Package | Variants |
|---|---|---|
| `AccountError` | accounts | `sealed class`: `NotFound(accountId)`, `AlreadyFrozen(accountId)`, `InsufficientFunds(accountId)` |
| `PaymentError` | payments | `sealed class`: `InvalidAccount(accountId)`, `InsufficientFunds(accountId)`, `DailyLimitExceeded(limit, attempted)` |

Note: `PaymentError.InsufficientFunds` is never thrown in the current `payments-core-svc` implementation.

## Consuming Services
- `accounts-core-svc` — uses `AccountResponse`, `AccountSummary`, `AccountStatus`, `AccountType`, `AccountError`, `MonetaryAmount`, `ApiError`, `PaginatedResponse`
- `payments-core-svc` — uses `PaymentRequest`, `PaymentResponse`, `PaymentError`, `PaymentStatus`, `PaymentType`, `AccountResponse`, `AccountStatus`, `MonetaryAmount`, `ApiError`
- `banking-bff` — uses all of the above; passes contract types through as-is (no local DTO duplication in the clean pattern)

## Serialization
`kotlinx-serialization-json:1.6.3` — all contract types annotated with `@Serializable`. Spring services
use Jackson for their own REST layer; banking-contracts types are annotated for both.

## Build and Publishing
- Gradle 8.5 / Kotlin 1.9.25 / Java 17 toolchain
- Group: `com.digitalbank`, artifact: `banking-contracts`, version: `0.0.1-SNAPSHOT`
- `maven-publish` plugin present but **no published artifact** — all consumers resolve via
  `includeBuild("../banking-contracts")` composite build. Must be co-located in the same workspace.
- Version is **unpinned** — a change to any type immediately affects all consuming services at next build.
