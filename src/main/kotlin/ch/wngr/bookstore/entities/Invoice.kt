package ch.wngr.bookstore.entities

import ch.wngr.bookstore.enums.PaymentMethod
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "invoice")
class Invoice (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,
    @OneToMany(mappedBy = "invoice")
    var sales: Set<Sale>? = HashSet(),

    val priceWithoutDiscount: Float = -1f,

    val priceWithDiscount: Float = -1f,

    @Enumerated(EnumType.STRING)
    var paymentMethod: PaymentMethod = PaymentMethod.CASH,

    val timeCreated: LocalDateTime = LocalDateTime.now(),

    val priceDiscountPercent: Int = 0,
    )