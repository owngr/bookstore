package ch.wngr.bookstore.entities

import ch.wngr.bookstore.annotations.EmptyConstructor
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "basket_book")
@IdClass(BasketBookId::class)
class BasketBook (

    @Id
    var basketId: Int = -1,


    @Id
    var bookId: Int = -1,


    var quantity: Int = 1
) {

}

@EmptyConstructor
data class BasketBookId(
    var basketId: Int,
    var bookId: Int,
) : Serializable