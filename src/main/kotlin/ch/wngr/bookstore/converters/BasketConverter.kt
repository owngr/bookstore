package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Basket
import ch.wngr.bookstore.models.BasketDto
import java.time.format.DateTimeFormatter

fun Basket.ToBasketDto() = BasketDto(
    dateClosed = timeClosed?.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")) ?: "",
    distributor = distributor?.name ?: ""
)