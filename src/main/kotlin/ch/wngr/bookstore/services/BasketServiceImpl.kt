package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.ToBasketDto
import ch.wngr.bookstore.entities.Basket
import ch.wngr.bookstore.entities.BasketBook
import ch.wngr.bookstore.entities.Distributor
import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.models.BasketRow
import ch.wngr.bookstore.models.SaleDTO
import ch.wngr.bookstore.repositories.BasketBookRepository
import ch.wngr.bookstore.repositories.BasketRepository
import ch.wngr.bookstore.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BasketServiceImpl @Autowired constructor(
    val basketRepository: BasketRepository,
    val basketBookRepository: BasketBookRepository,
    val bookRepository: BookRepository,
) : BasketService {
    override fun addBooksToBasket(saleDTOs: List<SaleDTO>) {
        for (sale in saleDTOs) {
            if (!sale.isbn.isNullOrEmpty()) {
                val book = bookRepository.findByIsbn(sale.isbn)
                if (book != null) {
                    val basket = getOrCreateBasket(book.distributor!!)
                    var basketBook: BasketBook
                    try {
                        basketBook = basket.basketBooks.first { bb -> bb.bookId == book.id }
                        basketBook.quantity += sale.quantity ?: 1
                    } catch (e: NoSuchElementException) {
                        basketBook = BasketBook(basket.id, book.id, sale.quantity ?: 1)
                        basket.basketBooks.add(basketBook)
                    }
                    basketBookRepository.save(basketBook)
                    basketRepository.save(basket)
                }
            }
        }

    }

    override fun getBaskets(open: Boolean): ResponseEntity<List<BasketDto>> {
        val baskets: List<Basket> = if (open) {
            basketRepository.findAllByTimeClosedIsNull()
        } else {
            basketRepository.findAllByTimeClosedIsNotNullOrderByTimeClosedDesc()
        }
        var basketRows: List<BasketRow>
        var basketDto: BasketDto
        val basketDtos = mutableListOf<BasketDto>()
        for (basket in baskets) {
            basketRows = basketBookRepository.findBasketDtoByBasket(basket.id)
            basketDto = basket.ToBasketDto()
            basketDto.books = basketRows
            basketDtos.add(basketDto)
        }
//        var basketRow = BasketRow(isbn = "", title = "", quantity = 1)
        return ResponseEntity(basketDtos, HttpStatus.OK)
    }

    override fun closeBasket(basketID: Int): ResponseEntity<BasketDto> {
        val basket =
            basketRepository.findById(basketID).orElse(null) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        basket.timeClosed = LocalDateTime.now()
        basketRepository.save(basket)
        return ResponseEntity(basket.ToBasketDto(), HttpStatus.OK)
    }

    private fun getOrCreateBasket(distributor: Distributor): Basket {
        var basket = basketRepository.findFirstByTimeClosedIsNullAndDistributorEquals(distributor)
        if (basket == null) {
            basket = Basket(distributor = distributor)
            basket = basketRepository.save(basket)
        }
        return basket
    }
}