package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.InvoiceRow
import ch.wngr.bookstore.models.SaleList
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

interface SaleService {

    fun sellBooks(saleList: SaleList): ResponseEntity<out Any>
    fun getInvoicesRow(startTime: LocalDateTime, endtime: LocalDateTime): ResponseEntity<List<InvoiceRow>>
    fun getInvoice(invoiceID: Int): ResponseEntity<SaleList>
}