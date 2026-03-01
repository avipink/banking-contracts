package com.digitalbank.contracts.accounts

import com.digitalbank.contracts.common.MonetaryAmount
import kotlinx.serialization.Serializable

/**
 * Lightweight account representation for list views and dashboard summaries.
 *
 * Intentionally omits verbose fields (transaction history, audit metadata, currency detail)
 * to keep list responses compact and performant. For full account detail, use [AccountResponse].
 *
 * **Produced by**: accounts-core-svc — `GET /api/v1/accounts`
 * **Consumed by**: banking-bff (dashboard account list)
 *
 * @property accountId Unique account identifier
 * @property accountType Product type classification
 * @property holderName Full name of the account holder
 * @property balance Current available balance
 * @property status Current operational status
 */
@Serializable
data class AccountSummary(
    val accountId: String,
    val accountType: AccountType,
    val holderName: String,
    val balance: MonetaryAmount,
    val status: AccountStatus
)
