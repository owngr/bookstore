package ch.wngr.bookstore.entities

import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
@Table(name = "basket")
class Basket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,

    @OneToMany(mappedBy = "basket")
    var basketBooks: MutableSet<BasketBook> = HashSet(),

    var timeClosed: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "distributor_id")
    var distributor: Distributor? = Distributor(),
)


