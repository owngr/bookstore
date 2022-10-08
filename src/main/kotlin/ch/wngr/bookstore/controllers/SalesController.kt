package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.InvoiceRow
import ch.wngr.bookstore.services.SaleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
@RequestMapping("/api/sales")
internal class SalesController @Autowired constructor(
    private val saleService: SaleService,
) {
    @GetMapping
    fun getInvoices(
        @RequestParam startTime: String,
        @RequestParam endTime: String
    ): ResponseEntity<List<InvoiceRow>> {
        return saleService.getInvoicesRow(
            LocalDateTime.ofInstant(Instant.parse(startTime), ZoneId.systemDefault()),
            LocalDateTime.ofInstant(Instant.parse(endTime), ZoneId.systemDefault())
        )
    }
}