package ch.wngr.bookstore.entities

import javax.persistence.*

@Entity
@Table(name="book")
class Book(
    val isbn: String,
    val title: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1
    ) {
    constructor() : this("","") {
    }
}

