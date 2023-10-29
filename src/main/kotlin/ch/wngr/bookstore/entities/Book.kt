package ch.wngr.bookstore.entities

import org.hibernate.engine.internal.Cascade
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "book")
data class Book(
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
    var price: Float? = null,
    var hasCover: Boolean = false,
    var amount: Int = 0,
    @ManyToMany(
        cascade = [CascadeType.ALL],
    )
    @JoinTable(
        name = "book_tag",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")],

    )
    var tags: MutableSet<Tag> = mutableSetOf(),
)
