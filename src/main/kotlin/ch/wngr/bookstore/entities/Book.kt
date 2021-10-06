package ch.wngr.bookstore.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

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
    var description: String = "",
    @ManyToOne
    @JoinColumn(name = "publisher")
    var publisher: Publisher? = Publisher(),
    @ManyToOne
    @JoinColumn(name = "distributor_id")
    var distributor: Distributor? = Distributor(),
)
