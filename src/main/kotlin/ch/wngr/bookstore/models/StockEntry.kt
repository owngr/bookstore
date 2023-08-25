package ch.wngr.bookstore.models

data class StockEntry(
    var isbn: String,
    var title: String = "",
    var authors: List<String>,
    var editor: String?,
    var distributor: String?,
    var amount: Int?,
    var description: String?,
    var price: Float?,
    var hasCover: Boolean,
    val tags: List<TagDto>,
)
