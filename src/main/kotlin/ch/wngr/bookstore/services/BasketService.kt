package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.models.SaleDTO
import org.springframework.http.ResponseEntity

interface BasketService {
    fun addBooksToBasket(saleDTOs: List<SaleDTO>)
    fun getBaskets(open: Boolean): ResponseEntity<List<BasketDto>>
    fun closeBasket(basketID: Int): ResponseEntity<BasketDto>
}