package ch.wngr.bookstore.models

import kotlinx.serialization.Serializable

@Serializable
data class ScraperBook(
    var isbn: String,
    var title: String = "",
    var authors: List<String>,
    var editor: String?,
    var distributor: String?,
    var description: String = "",
    var price: Float?,
    var coverUrl: String = "",
    var hasCover: Boolean,
    var amount: Int = 0,
    val tags: List<TagDto>,
){

    fun mergeScraperBooks(book: ScraperBook) {
        if (title == "") {
            title = book.title
        }
        if (authors.isEmpty()) {
            authors = book.authors
        }
        if (editor == null || editor == "") {
            editor = book.editor
        }
        if (description == "") {
            description = book.description
        }
        if (coverUrl == "") {
            coverUrl = book.coverUrl
        }
    }
    fun isBookComplete(): Boolean {
        return (title != ""
                && authors.isNotEmpty()
                && editor != null
                && description != ""
                && coverUrl != "")
    }

}

