package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.BasketBook
import ch.wngr.bookstore.entities.BasketBookId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BasketBookRepository : CrudRepository<BasketBook, BasketBookId>