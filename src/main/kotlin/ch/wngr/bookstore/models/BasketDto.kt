package ch.wngr.bookstore.models

data class BasketDto(
    var distributor: String = "",
    var books: List<BasketRow> = ArrayList()
)
