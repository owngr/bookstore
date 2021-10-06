package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.ScraperBook

fun Book.toScrapperBook() = ScraperBook(
    isbn = isbn,
    title = title,
    authors = authors.map(Author::toString),
    editor = publisher.toString(),
    distributor = "",
    description = description,
)
