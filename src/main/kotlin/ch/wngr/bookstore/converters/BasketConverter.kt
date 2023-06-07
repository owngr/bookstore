package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Basket
import ch.wngr.bookstore.models.BasketDto

fun Basket.ToBasketDto() = BasketDto(
    distributor = distributor?.name ?: ""
)