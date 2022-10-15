package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Publisher
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PublisherRepository : CrudRepository<Publisher, Int> {
    fun findByName(name: String): Publisher?
    fun findByOrderByName(): List<Publisher>
}