package ch.wngr.bookstore.entities

import javax.persistence.*

@Entity
@Table(name = "author")
class Author(
    var name: String = "",
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = -1,
    @ManyToMany(mappedBy = "authors")
    var books: Set<Book>? = HashSet()
) {
    constructor(name: String) : this(name, -1, null)

    override fun toString(): String {
        return this.name
    }
}
