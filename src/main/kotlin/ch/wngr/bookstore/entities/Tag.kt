package ch.wngr.bookstore.entities

import jakarta.persistence.*

@Entity
@Table(name = "tag")
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,

    var name: String = "",

    // primary is a reserved keyword
    var main: Boolean = false,

    @ManyToMany(mappedBy = "tags")
    var books: Set<Book>? = HashSet()
)
