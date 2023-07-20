package ch.wngr.bookstore.models

data class ScraperBook(
    var isbn: String,
    var title: String = "",
    val authors: List<String>,
    var editor: String?,
    var distributor: String?,
    var description: String = "",
    var price: Float?,
    var coverUrl: String = "",
    val amount: Int = 0,
    val tags: List<TagDto>,
)

