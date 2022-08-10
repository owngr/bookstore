package ch.wngr.bookstore.models

data class SaleDTO(
    val id: Int?,
    val isbn: String,
    val title: String?,
    val price: Float,
    val fullPrice: Float?,
    val quantity: Int?,
)

data class SaleList(
    val sales: List<SaleDTO>,
    val paymentMethod: String?,
)