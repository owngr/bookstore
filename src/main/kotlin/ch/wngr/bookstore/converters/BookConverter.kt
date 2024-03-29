package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Tag
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.ShopEntry

fun Book.toScrapperBook() = ScraperBook(
    isbn = isbn,
    title = title,
    authors = authors.map(Author::toString),
    editor = publisher.toString(),
    distributor = distributor.toString(),
    description = description,
    amount = amount,
    price = price,
    hasCover = hasCover,
    coverUrl = "/api/shop/$isbn",
    tags = tags.map(Tag::toTagDTO)
)



fun Book.toShopEntry() = ShopEntry(
    isbn = isbn,
    title = title,
    authors = authors.map(
        fun(author: Author): String {
            return author.name
        }
    ).toList(),
    editor = publisher?.name,
    description = description,
    price = price,
    available = amount > 0,
)