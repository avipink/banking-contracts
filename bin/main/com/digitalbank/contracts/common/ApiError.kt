package com.digitalbank.contracts.common

import kotlinx.serialization.Serializable

/**
 * Standard error envelope for all API error responses in the platform.
 *
 * All services return this type from their GlobalExceptionHandler, ensuring
 * a consistent error shape across the entire API boundary. Clients should
 * use [code] for programmatic error handling and [message] for display.
 *
 * **Produced by**: all services (via GlobalExceptionHandler)
 * **Consumed by**: banking-bff (error propagation and display)
 *
 * @property code Machine-readable error code (e.g., "ACCOUNT_NOT_FOUND", "DAILY_LIMIT_EXCEEDED")
 * @property message Human-readable error description suitable for logging
 * @property traceId Distributed trace identifier for cross-service log correlation
 * @property timestamp ISO 8601 timestamp of when the error occurred
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val traceId: String,
    val timestamp: String
)
