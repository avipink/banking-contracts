package com.digitalbank.contracts.common

import kotlinx.serialization.Serializable

/**
 * Standard audit fields for domain responses that require change tracking.
 *
 * Embedded in responses where consumers need to understand the record lifecycle.
 * The [version] field supports optimistic locking for concurrent modification scenarios.
 *
 * **Produced by**: accounts-core-svc, payments-core-svc (as embedded or response field)
 * **Consumed by**: banking-bff
 *
 * @property createdAt ISO 8601 timestamp of record creation
 * @property updatedAt ISO 8601 timestamp of last modification
 * @property version Monotonically increasing version counter for optimistic locking
 */
@Serializable
data class AuditMetadata(
    val createdAt: String,
    val updatedAt: String,
    val version: Long
)
