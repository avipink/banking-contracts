package com.digitalbank.contracts.common

import kotlinx.serialization.Serializable

/**
 * Represents a monetary amount with explicit currency denomination.
 *
 * The [amount] field is a [String] (not [Double]) to preserve full decimal precision
 * without floating-point rounding errors — critical for financial calculations.
 *
 * **Produced by**: accounts-core-svc, payments-core-svc
 * **Consumed by**: banking-bff, accounts-core-svc, payments-core-svc
 *
 * @property amount Decimal string representation (e.g., "1234.56")
 * @property currency ISO 4217 currency code (e.g., "USD", "EUR")
 */
@Serializable
data class MonetaryAmount(
    val amount: String,
    val currency: String
)
