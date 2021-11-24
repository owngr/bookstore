package ch.wngr.bookstore.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

@Service
class CoverServiceImpl @Autowired constructor() : CoverService {

    @Value("\${books.cover.directory:covers}")
    lateinit var bookCoverDirectory: String

    override fun uploadBookCover(image: MultipartFile) {
        val isbn = image.originalFilename
        val path = Path.of(bookCoverDirectory, "$isbn.jpg")
        Files.write(path, image.bytes)
    }

    override fun downloadBookCover(isbn: String): ByteArray {
        TODO("Not yet implemented")
    }
}
