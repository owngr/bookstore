package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.filters.TableSearchFilter
import ch.wngr.bookstore.models.SaleList
import ch.wngr.bookstore.models.StockEntry
import ch.wngr.bookstore.services.SaleService
import ch.wngr.bookstore.services.StockService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stock")
internal class StockController @Autowired constructor(
    private val stockService: StockService,
    private val saleService: SaleService,
) {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    @GetMapping("")
    fun getStock(
        @RequestParam lazyEvent: String
    ): Page<StockEntry>? {
        val searchFilter = json.decodeFromString<TableSearchFilter>(lazyEvent)
        return stockService.getStock(searchFilter)
    }

    @GetMapping("/{isbn}")
    fun getStockEntry(@PathVariable("isbn") isbn: String): StockEntry {
        return stockService.getStockEntry(isbn)
    }

    @PutMapping("")
    fun updateStock(@RequestBody stockEntry: StockEntry): StockEntry {
        return stockService.updateStock(stockEntry)
    }

    @DeleteMapping()
    fun deleteSTock() {
        return stockService.deleteStock()
    }

    @PutMapping("/missing")
    fun getMissingBook(@RequestBody sales: SaleList): ResponseEntity<List<StockEntry>> {
        return stockService.checkMissingBooks(sales.sales)
    }

    @PatchMapping()
    fun sellBooks(@RequestBody sales: SaleList): ResponseEntity<SaleList> {
        return saleService.sellBooks(sales)
    }

}
