package ch.wngr.bookstore.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class InvoiceRow(
    var id: Int,
    var quantity: Int,
    var price: Float,
    var timeCreated: String,
) {
    constructor(id: Int, quantity: Int, price: Float, timeCreated: LocalDateTime) : this(
        id,
        quantity,
        price,
        timeCreated.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:MM"))
    )
}
