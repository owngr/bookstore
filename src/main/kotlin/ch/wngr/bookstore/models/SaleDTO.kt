package ch.wngr.bookstore.models

data class SaleDTO(
    val id: Int?,
    val isbn: String?,
    val title: String,
    val price: Float,
    val fullPrice   : Float,
    val quantity: Int?,
    val new: Boolean,
)

data class SaleList(
    val sales: List<SaleDTO>,
    val paymentMethod: String?,
    val priceWithDiscount: Float,
    val priceWithoutDiscount: Float,
    val priceDiscountPercent: Int,
)