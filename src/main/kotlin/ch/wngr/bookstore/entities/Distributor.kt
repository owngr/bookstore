package ch.wngr.bookstore.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

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


