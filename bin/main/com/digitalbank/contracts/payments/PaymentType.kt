package com.digitalbank.contracts.payments

import kotlinx.serialization.Serializable

/**
 * Classifies the type of payment operation being performed.
 *
 * **Produced by**: payments-core-svc
 * **Consumed by**: banking-bff (payment history display, type-based filtering)
 */
@Serializable
enum class PaymentType {
    /** Transfer between two accounts within the same bank */
    INTERNAL_TRANSFER,

    /** Payment to an external biller (utilities, services, etc.) */
    BILL_PAYMENT,

    /** Payment scheduled for future execution */
    SCHEDULED
}
