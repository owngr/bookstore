package ch.wngr.bookstore.entities

import javax.persistence.*

@Entity
@Table(name="author")
class Author constructor(
    var name: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
)