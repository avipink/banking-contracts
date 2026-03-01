# Build and Test Summary — banking-contracts

## Quality Gate
- `./gradlew build` exits 0 (compile-clean)
- `./gradlew publishToMavenLocal` exits 0
- Artifact available at `com.digitalbank:banking-contracts:1.0.0` in mavenLocal

## No Unit Tests Required
This is a type-only contract library. There is no business logic to test.
Compile success is the quality gate. Downstream services verify correctness via their own integration tests.

## Downstream Consumption
After publishToMavenLocal, each service references the library via Gradle composite build:
```kotlin
// In each Spring Boot service's settings.gradle.kts:
includeBuild("../banking-contracts")
```
This means no `publishToMavenLocal` is needed during local development —
the composite build resolves the source directly.
