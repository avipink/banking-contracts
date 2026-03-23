package com.digitalbank.contracts.common

import kotlinx.serialization.Serializable

/**
 * Generic wrapper for paginated API responses.
 *
 * Provides a consistent pagination envelope across all list endpoints in the platform.
 * Consumers can use [totalPages] and [page] to implement pagination controls.
 *
 * **Produced by**: accounts-core-svc (account lists), payments-core-svc (payment lists)
 * **Consumed by**: banking-bff (dashboard list aggregation)
 *
 * @param T The type of items in the page
 * @property items The items on the current page
 * @property page Current page number (1-based)
 * @property pageSize Maximum number of items per page
 * @property totalItems Total number of items across all pages
 * @property totalPages Total number of pages available
 */
@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
)
