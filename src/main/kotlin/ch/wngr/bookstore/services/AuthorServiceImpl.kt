package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.repositories.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl @Autowired constructor(
    val authorRepository: AuthorRepository
) : AuthorService {

    override fun getOrCreateAuthor(authorName: String): Author {
        var author: Author? = authorRepository.findByName(authorName)
        if (author == null) {
            author = Author(authorName)
            author = authorRepository.save(author)
        }
        return author
    }
}