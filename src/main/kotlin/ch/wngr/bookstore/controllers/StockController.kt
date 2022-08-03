package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.StockEntry
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
@RequestMapping("/api/stock")
internal class StockController @Autowired constructor(
    private val stockService: StockService,
) {
    @GetMapping("")
    fun getStock(): List<StockEntry> {
        return stockService.getStock()
    }

    @GetMapping("/{isbn}")
    fun getStockEntry(@PathVariable("isbn") isbn: String): StockEntry {
        return stockService.getStockEntry(isbn)
    }

    @PutMapping("")
    fun updateStock(@RequestBody stockEntry: StockEntry): StockEntry {
//        LocalDate.now().atStartOfDay(ZoneId.systemDefault())
//        LocalDateTime.now().toEpochSecond()
        return stockService.updateStock(stockEntry)
    }

    @DeleteMapping()
    fun deleteSTock() {
        return stockService.deleteStock()
    }
}
