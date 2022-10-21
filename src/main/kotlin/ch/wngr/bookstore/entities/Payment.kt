package ch.wngr.bookstore.entities

import ch.wngr.bookstore.enums.PaymentMethod
import javax.persistence.*

@Entity
@Table(name = "payment")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null,

    @Enumerated(EnumType.STRING)
    var paymentMethod: PaymentMethod = PaymentMethod.CASH,

    val price: Float = -1f,
)