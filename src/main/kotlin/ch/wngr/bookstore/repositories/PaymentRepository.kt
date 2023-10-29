package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Payment
import ch.wngr.bookstore.models.InvoiceRow
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PaymentRepository : CrudRepository<Payment, Int> {

    @Query(
        "select new ch.wngr.bookstore.models.InvoiceRow(i.id, count(s), p.price, p.paymentMethod, i.timeCreated) " +
                "from Sale s " +
                "join Invoice i on s.invoice = i " +
                "join Payment p on p.invoice = i " +
                "where i.timeCreated between :startTime and :endTime " +
                "group by i.id, p.price, p.paymentMethod, i.timeCreated"
    )
    fun getInvoiceByTimeCreatedBetween(
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<InvoiceRow>
}