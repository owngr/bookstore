package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Sale
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SaleRepository : CrudRepository<Sale, Int> {
}