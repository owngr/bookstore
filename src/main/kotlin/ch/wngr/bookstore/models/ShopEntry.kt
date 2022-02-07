package ch.wngr.bookstore.models

data class ShopEntry(
    var isbn: String,
    var title: String = "",
    var authors: List<String>,
    var editor: String?,
    var description: String?,
    var price: Float?,
    var available: Boolean,
)
