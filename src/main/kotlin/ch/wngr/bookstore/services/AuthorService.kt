package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Author

interface AuthorService {
    fun getOrCreateAuthor(authorName: String): Author
}