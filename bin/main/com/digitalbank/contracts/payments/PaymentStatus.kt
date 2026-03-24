package com.digitalbank.contracts.payments

import kotlinx.serialization.Serializable

/**
 * Represents the lifecycle state of a payment.
 *
 * State transitions: PENDING → COMPLETED | FAILED | CANCELLED
 * Terminal states (COMPLETED, FAILED, CANCELLED) are immutable once reached.
 *
 * **Produced by**: payments-core-svc
 * **Consumed by**: banking-bff (payment status display and polling)
 */
@Serializable
enum class PaymentStatus {
    /** Payment has been accepted and is awaiting processing */
    PENDING,

    /** Payment has been successfully executed */
    COMPLETED,

    /** Payment failed due to a processing error or validation failure */
    FAILED,

    /** Payment was cancelled before execution */
    CANCELLED
}
