package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toSaleList
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Invoice
import ch.wngr.bookstore.entities.Payment
import ch.wngr.bookstore.entities.Sale
import ch.wngr.bookstore.enums.PaymentMethod
import ch.wngr.bookstore.models.InvoiceRow
import ch.wngr.bookstore.models.SaleList
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.InvoiceRepository
import ch.wngr.bookstore.repositories.PaymentRepository
import ch.wngr.bookstore.repositories.SaleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SaleServiceImpl @Autowired constructor(
    val invoiceRepository: InvoiceRepository,
    val saleRepository: SaleRepository,
    val stockService: StockService,
    val bookRepository: BookRepository,
    val paymentRepository: PaymentRepository,
    val basketService: BasketService,
) : SaleService {

    override fun sellBooks(saleList: SaleList): ResponseEntity<out Any> {
        if (stockService.getMissingBooks(saleList.sales).isNotEmpty()) {
            return ResponseEntity(null, HttpStatus.CONFLICT)
        }
        stockService.removeBooks(saleList.sales)
        try {
            basketService.addBooksToBasket(saleList.sales)
        } catch (e : Exception) {
            return ResponseEntity(e, HttpStatus.CONFLICT)
        }
        val sales: MutableSet<Sale> = HashSet()
        var invoice =
            Invoice(
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
        for (paymentDTO in saleList.paymentMethod!!) {
            val payment = Payment(
                invoice = invoice,
                price = paymentDTO.price,
                paymentMethod = PaymentMethod.valueOf(paymentDTO.value)

            )
            paymentRepository.save(payment)
        }
        return ResponseEntity(saleList, HttpStatus.OK)
    }

    override fun getInvoicesRow(startTime: LocalDateTime, endtime: LocalDateTime): ResponseEntity<List<InvoiceRow>> {
        return ResponseEntity(paymentRepository.getInvoiceByTimeCreatedBetween(startTime, endtime), HttpStatus.OK)
    }

    override fun getInvoice(invoiceID: Int): ResponseEntity<SaleList> {
        return ResponseEntity(invoiceRepository.findById(invoiceID).get().toSaleList(), HttpStatus.OK)
    }
}