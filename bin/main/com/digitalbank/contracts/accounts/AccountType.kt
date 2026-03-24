package com.digitalbank.contracts.accounts

import kotlinx.serialization.Serializable

/**
 * Classifies the account product type offered by the bank.
 *
 * **Produced by**: accounts-core-svc
 * **Consumed by**: banking-bff (dashboard display, type-based filtering)
 */
@Serializable
enum class AccountType {
    /** Standard transactional account for everyday banking */
    CHECKING,

    /** Interest-bearing account for savings goals */
    SAVINGS,

    /** Higher-yield account with limited monthly transactions */
    MONEY_MARKET
}
