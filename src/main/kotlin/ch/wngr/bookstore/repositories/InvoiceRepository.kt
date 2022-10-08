package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Invoice
import ch.wngr.bookstore.models.InvoiceRow
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface InvoiceRepository : CrudRepository<Invoice, Int> {

    @Query("select new ch.wngr.bookstore.models.InvoiceRow(i.id, i.sales.size, i.priceWithDiscount, i.timeCreated) " +
            "from Invoice i " +
            "where i.timeCreated between :startTime and :endTime")
    fun getInvoiceByTimeCreatedBetween(
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<InvoiceRow>
}