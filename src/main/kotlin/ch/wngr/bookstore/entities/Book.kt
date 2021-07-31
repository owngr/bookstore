package ch.wngr.bookstore.entities

import javax.persistence.*

@Entity
@Table(name = "book")
class Book(
    var isbn: String = "",
    var title: String = "",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,
    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    var authors: Set<Author> = HashSet(),
)
