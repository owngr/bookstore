package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.BasketBook
import ch.wngr.bookstore.entities.BasketBookId
import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.models.BasketRow
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BasketBookRepository : CrudRepository<BasketBook, BasketBookId> {

    @Query(value = "SELECT new ch.wngr.bookstore.models.BasketRow(b.isbn, b.title, bb.quantity) " +
            "from BasketBook bb " +
            "join Book b on bb.bookId = b.id" +
            " where bb.basketId = :basketId ")
    fun findBasketDtoByBasket(basketId: Int): List<BasketRow>
}