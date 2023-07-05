package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.BasketBook
import ch.wngr.bookstore.entities.BasketBookId
import ch.wngr.bookstore.entities.Tag
import ch.wngr.bookstore.models.BasketDto
import ch.wngr.bookstore.models.BasketRow
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : CrudRepository<Tag, Int> {

    fun findByOrderByMainAscNameAsc(): List<Tag>


}