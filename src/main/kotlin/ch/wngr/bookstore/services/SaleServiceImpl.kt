package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Invoice
import ch.wngr.bookstore.entities.Sale
import ch.wngr.bookstore.enums.PaymentMethod
import ch.wngr.bookstore.models.SaleDTO
import ch.wngr.bookstore.models.SaleList
import ch.wngr.bookstore.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ch.wngr.bookstore.repositories.InvoiceRepository
import ch.wngr.bookstore.repositories.SaleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Service
class SaleServiceImpl @Autowired constructor(
    val invoiceRepository: InvoiceRepository,
    val saleRepository: SaleRepository,
    val stockService: StockService,
    val bookRepository: BookRepository,
) : SaleService {

    override fun sellBooks(saleList: SaleList): ResponseEntity<SaleList> {
        if (stockService.getMissingBooks(saleList.sales).isNotEmpty()) {
            return ResponseEntity(null, HttpStatus.CONFLICT)
        }
        stockService.removeBooks(saleList.sales)
        var sales: MutableSet<Sale> = HashSet()
        var invoice = Invoice(paymentMethod = PaymentMethod.valueOf(saleList.paymentMethod.toString()))
        invoice = invoiceRepository.save(invoice)
        for (saleDTO in saleList.sales) {
            val book = bookRepository.findByIsbn(saleDTO.isbn)
            // if book has no price we set fullprice on false
            var fullPrice = false
            if (book != null && book.price != null) {
                fullPrice = (book.price == saleDTO.price)
            }
            val sale = Sale(
                book = book,
                price = saleDTO.price,
                fullPrice = fullPrice,
                invoice = invoice,
            )
            sales.add(sale)
            saleRepository.save(sale)
        }
        return ResponseEntity(saleList, HttpStatus.OK)
    }
}