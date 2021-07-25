package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Author
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CrudRepository<Author, Int>{
    fun findByName(name: String): Author?
}