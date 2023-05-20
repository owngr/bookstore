package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.SaleDTO

interface BasketService {
    fun addBooksToBasket(saleDTOs: List<SaleDTO>)
}