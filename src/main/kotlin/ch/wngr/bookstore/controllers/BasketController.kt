package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.services.BasketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/basket")
class BasketController @Autowired constructor(
    private val basketService: BasketService
){

    @GetMapping
    fun fetchBaskets(
        @RequestParam open: Boolean,
    ): ResponseEntity<List<BasketDto>> {
        return basketService.getBaskets(open)
    }
}