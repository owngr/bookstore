package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.models.ScraperBook
import org.springframework.stereotype.Repository

@Repository
interface BookInfoRepository {

    fun createBook(book: ScraperBook);
}