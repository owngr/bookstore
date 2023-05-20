package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.BasketBook
import ch.wngr.bookstore.models.SaleDTO
import ch.wngr.bookstore.repositories.BasketBookRepository
import ch.wngr.bookstore.repositories.BasketRepository
import ch.wngr.bookstore.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BasketServiceImpl @Autowired constructor(
    val basketRepository: BasketRepository,
    val basketBookRepository: BasketBookRepository,
    val bookRepository: BookRepository,
) : BasketService {
    override fun addBooksToBasket(saleDTOs: List<SaleDTO>) {
        val basket = basketRepository.findFirstByTimeClosedIsNull()
        for (sale in saleDTOs) {
            if (!sale.isbn.isNullOrEmpty()) {
                val book = bookRepository.findByIsbn(sale.isbn)
                if (book != null) {
                    var basketBook: BasketBook
                    try {
                        basketBook = basket.basketBooks.first { bb -> bb.bookId == book.id }
                        basketBook.quantity += sale.quantity ?:1
                    } catch (e: NoSuchElementException) {
                        basketBook = BasketBook(basket.id, book.id,  sale.quantity ?: 1)
                        basket.basketBooks.add(basketBook)
                    }
                    basketBookRepository.save(basketBook)
                    basketRepository.save(basket)
                }
            }
        }

    }
}