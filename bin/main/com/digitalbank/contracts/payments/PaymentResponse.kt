package com.digitalbank.contracts.payments

import com.digitalbank.contracts.common.MonetaryAmount
import kotlinx.serialization.Serializable

/**
 * Payment record returned after initiation and for all subsequent status queries.
 *
 * This is the canonical representation of a payment at the API boundary.
 * Internal processing fields (retry count, processing node, queue position, etc.)
 * are never included here — they remain private to payments-core-svc.
 *
 * **Produced by**: payments-core-svc — `POST /api/v1/payments`,
 *   `GET /api/v1/payments/{id}`,
 *   `GET /api/v1/payments/account/{accountId}`
 * **Consumed by**: banking-bff (dashboard payment history, transfer result display)
 *
 * @property paymentId Unique payment identifier
 * @property fromAccountId Source account identifier
 * @property toAccountId Destination account identifier
 * @property amount Amount and currency transferred
 * @property type Classification of the payment operation
 * @property status Current lifecycle state of the payment
 * @property reference Optional customer-defined reference or memo (nullable)
 * @property createdAt ISO 8601 timestamp when the payment record was created
 */
@Serializable
data class PaymentResponse(
    val paymentId: String,
    val fromAccountId: String,
    val toAccountId: String,
    val amount: MonetaryAmount,
    val type: PaymentType,
    val status: PaymentStatus,
    val reference: String?,
    val createdAt: String
)
