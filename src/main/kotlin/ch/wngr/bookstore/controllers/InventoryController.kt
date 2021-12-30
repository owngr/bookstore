package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.Inventory
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inventory")
class InventoryController @Autowired constructor(
    private val stockService: StockService
) {
    @GetMapping
    fun fetchInventory(): Inventory {
        return stockService.getInventory()
    }
}
