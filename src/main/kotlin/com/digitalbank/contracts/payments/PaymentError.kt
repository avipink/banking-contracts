package com.digitalbank.contracts.payments

import com.digitalbank.contracts.common.MonetaryAmount

/**
 * Typed error domain for the Payments bounded context.
 *
 * Use exhaustive `when` expressions to handle all sub-types without fallback branches.
 * payments-core-svc's GlobalExceptionHandler maps each sub-type to the appropriate HTTP
 * status code and [com.digitalbank.contracts.common.ApiError] response body.
 *
 * **Produced by**: payments-core-svc (thrown as domain exceptions)
 * **Consumed by**: payments-core-svc (GlobalExceptionHandler),
 *   banking-bff (error propagation to dashboard clients)
 */
sealed class PaymentError {

    /**
     * The source account has insufficient balance for the requested payment.
     *
     * HTTP mapping: 422 Unprocessable Entity
     *
     * @property accountId The account that lacks sufficient funds
     * @property requested The amount that was requested
     * @property available The amount currently available in the account
     */
    data class InsufficientFunds(
        val accountId: String,
        val requested: MonetaryAmount,
        val available: MonetaryAmount
    ) : PaymentError()

    /**
     * The specified account does not exist or failed validation in accounts-core-svc.
     *
     * HTTP mapping: 404 Not Found
     *
     * @property accountId The account ID that failed validation
     */
    data class InvalidAccount(val accountId: String) : PaymentError()

    /**
     * The payment amount would exceed the configured daily transaction limit.
     *
     * HTTP mapping: 422 Unprocessable Entity
     *
     * @property limit The configured daily limit for the account
     * @property attempted The total amount attempted including this payment
     */
    data class DailyLimitExceeded(
        val limit: MonetaryAmount,
        val attempted: MonetaryAmount
    ) : PaymentError()
}
