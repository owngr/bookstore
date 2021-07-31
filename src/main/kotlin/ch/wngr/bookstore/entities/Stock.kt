package ch.wngr.bookstore.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "stock")
class Stock(
    var amount: Int = 0,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,
    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", unique = true)
    var book: Book = Book(),
)
