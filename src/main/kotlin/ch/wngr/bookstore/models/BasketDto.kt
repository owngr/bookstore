package ch.wngr.bookstore.models

data class BasketDto(
    var id: Int = -1,
    var dateClosed: String = "",
    var distributor: String = "",
    var books: List<BasketRow> = ArrayList()
)
