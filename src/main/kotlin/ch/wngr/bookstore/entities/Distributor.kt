package ch.wngr.bookstore.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "distributor")
class Distributor(
    var name: String = "",
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = -1,
) {
    constructor(name: String) : this(name, -1)

    override fun toString(): String {
        return this.name
    }
}


