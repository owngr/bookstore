package ch.wngr.bookstore.filters

import ch.wngr.bookstore.models.TagDto
import kotlinx.serialization.Serializable
import org.springframework.data.domain.Sort.Direction

@Serializable
data class TableFilter(val value: String?, val matchMode: String)

@Serializable
data class TagFilter(val value: List<TagDto>, val matchMode: String)

@Serializable
data class StockFilter(
    val global: TableFilter = TableFilter(null, "contains"),
    val tags: TagFilter = TagFilter(ArrayList(), "contains"),
)

@Serializable
data class TableSearchFilter(
    val first: Int,
    val rows: Int,
    val sortField: String = "isbn",
    val sortOrder: Int?,
    val filters: StockFilter
) {
    fun getSortDirection(): Direction {
        if (sortOrder == null) {
            return Direction.ASC
        } else if (sortOrder == -1) {
            return Direction.DESC
        }
        return Direction.ASC
    }
}
