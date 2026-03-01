package com.digitalbank.contracts.accounts

import kotlinx.serialization.Serializable

/**
 * Represents the operational lifecycle state of a bank account.
 *
 * payments-core-svc validates this status before processing any transaction.
 * Accounts in [FROZEN] or [CLOSED] state reject all debits and credits.
 *
 * **Produced by**: accounts-core-svc
 * **Consumed by**: banking-bff, payments-core-svc (account validation)
 */
@Serializable
enum class AccountStatus {
    /** Account is open and fully operational */
    ACTIVE,

    /** Account has had no activity for an extended period */
    DORMANT,

    /** Account is locked; all debits and credits are blocked */
    FROZEN,

    /** Account has been permanently closed */
    CLOSED
}
