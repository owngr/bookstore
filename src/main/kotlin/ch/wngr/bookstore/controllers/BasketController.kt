package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.services.BasketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/basket")
class BasketController @Autowired constructor(
    private val basketService: BasketService
) {

    @GetMapping
    fun fetchBaskets(
        @RequestParam open: Boolean,
    ): ResponseEntity<List<BasketDto>> {
        return basketService.getBaskets(open)
    }

    @DeleteMapping("/{basketID}")
    fun closeBasket(@PathVariable basketID: Int): ResponseEntity<BasketDto> {
        return basketService.closeBasket(basketID)
    }
}