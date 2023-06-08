package ch.wngr.bookstore.models

data class BasketDto(
    var dateClosed: String = "",
    var distributor: String = "",
    var books: List<BasketRow> = ArrayList()
)
