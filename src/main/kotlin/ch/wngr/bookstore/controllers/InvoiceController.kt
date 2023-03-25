package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.SaleList
import ch.wngr.bookstore.services.SaleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/invoices")
class InvoiceController @Autowired constructor(
    private val saleService: SaleService
) {

    @GetMapping("/{invoiceID}")
    fun getInvoice(@PathVariable invoiceID: Int): ResponseEntity<SaleList> {
        return saleService.getInvoice(invoiceID)
    }
}