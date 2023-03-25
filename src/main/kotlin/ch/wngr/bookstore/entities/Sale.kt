package ch.wngr.bookstore.entities

import javax.persistence.*

@Entity
@Table(name = "sale")
class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,
    @ManyToOne
    @JoinColumn(name = "book_id")
    var book: Book? = Book(),

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null,

    var price: Float = 0F,
    var fullPrice: Float = 0F,

    var description: String = "",

    var secondHand: Boolean = false,

    )