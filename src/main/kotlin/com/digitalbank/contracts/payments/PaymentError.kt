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

    /**
     * One or more request fields failed service-layer validation.
     *
     * HTTP mapping: 400 Bad Request
     *
     * Validation is performed in the service layer (not via Jakarta Validation annotations
     * on the contract type) to avoid introducing a transitive dependency on the validation
     * API for all consumers of banking-contracts.
     *
     * @property fieldErrors Map of field name to human-readable error message
     *   (e.g., mapOf("amount" to "must be > 0", "fromAccountId" to "must not be blank"))
     */
    data class ValidationFailed(
        val fieldErrors: Map<String, String>
    ) : PaymentError()

    /**
     * The provided idempotency key was already used for a payment request with a
     * different payload. Per the Stripe idempotency pattern, the same key may only
     * be reused if the request payload is identical.
     *
     * HTTP mapping: 409 Conflict
     *
     * @property key The idempotency key that was reused with a conflicting payload
     */
    data class IdempotencyKeyReused(
        val key: String
    ) : PaymentError()

    /**
     * The currency of the payment request does not match the currency of the source account.
     * Multi-currency conversion is not supported on this platform.
     *
     * HTTP mapping: 422 Unprocessable Entity
     *
     * @property requested The currency code specified in the payment request
     * @property accountCurrency The currency code of the source account
     */
    data class CurrencyMismatch(
        val requested: String,
        val accountCurrency: String
    ) : PaymentError()
}
