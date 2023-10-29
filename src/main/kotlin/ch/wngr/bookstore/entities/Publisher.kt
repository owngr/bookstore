package ch.wngr.bookstore.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "publisher")
class Publisher(
    var name: String = "",
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = -1,
    @ManyToOne
    @JoinColumn(name = "default_distributor_id")
    var defaultDistributor: Distributor? = Distributor(),
) {

    override fun toString(): String {
        return this.name
    }
}


