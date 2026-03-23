package com.digitalbank.contracts.accounts

import com.digitalbank.contracts.common.MonetaryAmount

/**
 * Typed error domain for the Accounts bounded context.
 *
 * Use exhaustive `when` expressions to handle all sub-types without fallback branches.
 * accounts-core-svc's GlobalExceptionHandler maps each sub-type to the appropriate HTTP
 * status code and [com.digitalbank.contracts.common.ApiError] response body.
 *
 * **Produced by**: accounts-core-svc (thrown as domain exceptions)
 * **Consumed by**: accounts-core-svc (GlobalExceptionHandler),
 *   payments-core-svc (account validation error handling)
 */
sealed class AccountError {

    /**
     * No account exists with the specified identifier.
     *
     * HTTP mapping: 404 Not Found
     *
     * @property accountId The ID that was not found
     */
    data class NotFound(val accountId: String) : AccountError()

    /**
     * The account has insufficient balance for the requested operation.
     *
     * HTTP mapping: 422 Unprocessable Entity
     *
     * @property accountId The account that lacks funds
     * @property requested The amount that was requested
     * @property available The amount currently available
     */
    data class InsufficientFunds(
        val accountId: String,
        val requested: MonetaryAmount,
        val available: MonetaryAmount
    ) : AccountError()

    /**
     * The account is frozen and cannot process any transactions.
     *
     * HTTP mapping: 409 Conflict
     *
     * @property accountId The frozen account identifier
     */
    data class AccountFrozen(val accountId: String) : AccountError()
}
