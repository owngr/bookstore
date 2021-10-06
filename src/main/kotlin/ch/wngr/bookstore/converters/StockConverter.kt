package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.StockEntry

fun Stock.toStockEntry() = StockEntry(
    isbn = book.isbn,
    title = book.title,
    authors = book.authors.map(
        fun(author: Author): String {
            return author.name
        }
    ).toList(),
    editor = book.publisher?.name,
    distributor = null,
    amount = amount,
    description = book.description
)
