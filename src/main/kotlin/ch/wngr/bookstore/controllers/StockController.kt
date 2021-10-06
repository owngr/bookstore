package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.StockEntry
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock")
internal class StockController @Autowired constructor(
    private val stockService: StockService,
) {
    @GetMapping("")
    fun getStock(): List<StockEntry> {
        return stockService.getStock()
    }

    @PutMapping("")
    fun updateStock(@RequestBody stockEntry: StockEntry): StockEntry {
        return stockService.updateStock(stockEntry)
    }
}
