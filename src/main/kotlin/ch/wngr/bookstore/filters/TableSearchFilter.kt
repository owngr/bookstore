package ch.wngr.bookstore.filters

import kotlinx.serialization.Serializable
import org.springframework.data.domain.Sort.Direction

@Serializable
data class TableFilter(val value: String?, val matchMode: String)

@Serializable
data class GlobalFilter(val global: TableFilter = TableFilter(null, "contains"))

@Serializable
data class TableSearchFilter (
    val first: Int,
    val rows: Int,
    val sortField: String = "isbn",
    val sortOrder: Int?,
    val filters: GlobalFilter
) {
    fun getSortDirection() : Direction {
        if (sortOrder == null) {
            return Direction.ASC
        } else if (sortOrder == -1) {
            return Direction.DESC
        }
        return Direction.ASC
    }
}
