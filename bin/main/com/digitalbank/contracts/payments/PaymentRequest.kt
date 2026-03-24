package com.digitalbank.contracts.payments

import com.digitalbank.contracts.common.MonetaryAmount
import kotlinx.serialization.Serializable

/**
 * Inbound payment initiation request.
 *
 * Sent by banking-bff to payments-core-svc when a customer initiates a transfer
 * from the dashboard. The payments-core-svc validates both accounts via
 * accounts-core-svc before processing.
 *
 * **Produced by**: banking-bff (forwarded from `POST /api/v1/dashboard/transfer`)
 * **Consumed by**: payments-core-svc — `POST /api/v1/payments`
 *
 * @property fromAccountId Source account identifier
 * @property toAccountId Destination account identifier
 * @property amount Amount and currency to transfer
 * @property type Classification of the payment operation
 * @property reference Optional customer-defined reference or memo (nullable)
 */
@Serializable
data class PaymentRequest(
    val fromAccountId: String,
    val toAccountId: String,
    val amount: MonetaryAmount,
    val type: PaymentType,
    val reference: String?
)
