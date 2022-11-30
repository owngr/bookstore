package ch.wngr.bookstore.models

import ch.wngr.bookstore.enums.PaymentMethod
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class InvoiceRow(
    var id: Int,
    var quantity: Int,
    var price: Float,
    var paymentOption: String,
    var timeCreated: String,
) {
    constructor(id: Int, quantity: Int, price: Float, paymentOption: PaymentMethod, timeCreated: LocalDateTime) : this(
        id,
        quantity,
        price,
        paymentOption.toString(),
        timeCreated.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm"))
    )
}
