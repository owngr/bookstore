package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.ShopEntry
import ch.wngr.bookstore.models.StockEntry

fun Book.toScrapperBook() = ScraperBook(
    isbn = isbn,
    title = title,
    authors = authors.map(Author::toString),
    editor = publisher.toString(),
    distributor = "",
    description = description,
    price = price,
)

fun Book.toStockEntry() = StockEntry(
    isbn = isbn,
    title = title,
    authors = authors.map(
        fun(author: Author): String {
            return author.name
        }
    ).toList(),
    editor = publisher?.name,
    distributor = distributor?.toString(),
    amount = amount,
    description = description,
    price = price,
    hasCover = hasCover,
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