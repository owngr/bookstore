package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Basket
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BasketRepository: CrudRepository<Basket, Int> {
    fun findFirstByTimeClosedIsNull(): Basket
}