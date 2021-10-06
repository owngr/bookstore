package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Distributor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DistributorRepository : CrudRepository<Distributor, Int> {
    fun findByName(name: String): Distributor?
}
