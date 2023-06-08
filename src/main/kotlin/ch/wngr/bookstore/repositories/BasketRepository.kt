package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Basket
import ch.wngr.bookstore.entities.Distributor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BasketRepository : CrudRepository<Basket, Int> {
    fun findFirstByTimeClosedIsNullAndDistributorEquals(distributor: Distributor): Basket?

    fun findAllByTimeClosedIsNull(): List<Basket>

    fun findAllByTimeClosedIsNotNullOrderByTimeClosedDesc(): List<Basket>

}