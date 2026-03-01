# Requirement Verification Questions — banking-contracts

> Requirements sourced from Practice Lab Part 3 Prompt 1 are complete and unambiguous.
> Only the mandatory Security Extension applicability question requires your input.

---

## Question 1: Security Extension

Should security extension rules be enforced for this project?

**Context**: `banking-contracts` is a **pure Kotlin library** with no HTTP endpoints, no authentication,
no database, no network calls, and no deployed runtime. Most SECURITY rules (SECURITY-01 through
SECURITY-09, SECURITY-11 through SECURITY-15) will be **N/A** for this artifact type.
The rules that do apply are:
- **SECURITY-05** (Input validation) — type-safe data classes enforce this by design
- **SECURITY-10** (Supply chain) — dependency pinning in `build.gradle.kts`

A) Yes — enforce all SECURITY rules as blocking constraints; mark inapplicable rules as N/A
   (recommended — banking domain, production library consumed by regulated services)

B) No — skip all SECURITY rules
   (suitable for PoCs/prototypes)

X) Other (please describe after [Answer]: tag below)

[Answer]:B
