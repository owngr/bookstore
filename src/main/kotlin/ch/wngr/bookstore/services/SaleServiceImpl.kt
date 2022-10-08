package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Invoice
import ch.wngr.bookstore.entities.Sale
import ch.wngr.bookstore.enums.PaymentMethod
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
        val sales: MutableSet<Sale> = HashSet()
        var invoice =
            Invoice(
                paymentMethod = PaymentMethod.valueOf(saleList.paymentMethod.toString()),
                priceWithDiscount = saleList.priceWithDiscount,
                priceWithoutDiscount = saleList.priceWithoutDiscount,
                priceDiscountPercent = saleList.priceDiscountPercent
            )
        invoice = invoiceRepository.save(invoice)
        for (saleDTO in saleList.sales) {
            var book: Book? = null
            if (saleDTO.isbn != null) {
                book = bookRepository.findByIsbn(saleDTO.isbn)
            }
            // if book has no price we set fullprice on false
            val sale = Sale(
                book = book,
                price = saleDTO.price,
                fullPrice = saleDTO.fullPrice,
                invoice = invoice,
                description = saleDTO.title,
                secondHand = !saleDTO.new,
            )
            sales.add(sale)
            saleRepository.save(sale)
        }
        return ResponseEntity(saleList, HttpStatus.OK)
    }
}