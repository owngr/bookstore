package ch.wngr.bookstore.models

data class BasketRow(
    var isbn: String = "",
    var title: String = "",
    var quantity: Int = 1,
)
