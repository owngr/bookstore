package ch.wngr.bookstore.services

import ch.wngr.bookstore.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

@Service
class CoverServiceImpl @Autowired constructor(
    val bookRepository: BookRepository
) : CoverService {

    @Value("\${books.cover.directory:covers}")
    lateinit var bookCoverDirectory: String

    override fun uploadBookCover(image: MultipartFile, isbn: String) {
        val path = Path.of(bookCoverDirectory, "$isbn.jpg")
        Files.write(path, image.bytes)
        val book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            book.hasCover = true
            bookRepository.save(book)
        }
    }

    override fun downloadBookCover(isbn: String): ByteArray {
        TODO("Not yet implemented")
    }
}
