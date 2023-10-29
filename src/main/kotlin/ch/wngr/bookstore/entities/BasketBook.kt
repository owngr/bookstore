package ch.wngr.bookstore.entities

import ch.wngr.bookstore.annotations.EmptyConstructor
import java.io.Serializable
import jakarta.persistence.*

@Entity
@Table(name = "basket_book")
@IdClass(BasketBookId::class)
class BasketBook (


    @ManyToOne
    @JoinColumn(name = "basket_id")
    var basket: Basket,


    @Id
    var bookId: Int = -1,


    var quantity: Int = 1
) {

}

@EmptyConstructor
data class BasketBookId(
    var basket: Basket,
    var bookId: Int,
) : Serializable