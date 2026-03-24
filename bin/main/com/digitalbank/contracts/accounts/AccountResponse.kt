package com.digitalbank.contracts.accounts

import com.digitalbank.contracts.common.MonetaryAmount
import kotlinx.serialization.Serializable

/**
 * Full account detail response including all consumer-visible fields.
 *
 * Internal domain fields (internalRiskScore, kycStatus, createdBy, etc.) are
 * NEVER included here. The accounts-core-svc mapper is responsible for the
 * projection from the internal domain model to this contract type.
 *
 * **Produced by**: accounts-core-svc — `GET /api/v1/accounts/{id}`,
 *   `POST /api/v1/accounts/{id}/hold`
 * **Consumed by**: banking-bff (account detail view, hold confirmation)
 *
 * @property accountId Unique account identifier
 * @property accountType Product type classification
 * @property holderName Full name of the account holder
 * @property balance Current available balance
 * @property status Current operational status
 * @property currency ISO 4217 currency of the account (e.g., "USD")
 * @property lastTransactionDate ISO 8601 date of the most recent transaction; null if no transactions
 * @property createdAt ISO 8601 timestamp when the account was opened
 */
@Serializable
data class AccountResponse(
    val accountId: String,
    val accountType: AccountType,
    val holderName: String,
    val balance: MonetaryAmount,
    val status: AccountStatus,
    val currency: String,
    val lastTransactionDate: String?,
    val createdAt: String
)
