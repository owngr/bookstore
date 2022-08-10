package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Invoice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository : CrudRepository<Invoice, Int> {
}