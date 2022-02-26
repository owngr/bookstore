package ch.wngr.bookstore.services

import ch.wngr.bookstore.repositories.BookRepository
import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists

@Service
class CoverServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
) : CoverService {

    @Value("\${books.cover.directory:covers}")
    lateinit var bookCoverDirectory: String

    @Value("\${books.cover.tempDirectory:/tmp}")
    lateinit var coverTempDirectory: String

    val imageHeight = 600

    val imageWidth = 400

    val jpegQuality = 80

    override fun uploadBookCover(image: MultipartFile, isbn: String) {
        convertImage(image, isbn)
        val book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            book.hasCover = true
            bookRepository.save(book)
        }
    }

    override fun downloadBookCover(isbn: String): ByteArray {
        val inputStream: InputStream? = Files.newInputStream(Path.of(bookCoverDirectory, "$isbn.jpeg"))
        return inputStream!!.readBytes()
    }

    private fun convertImage(image: MultipartFile, isbn: String) {
        val extension = image.name.substringAfterLast(".", "")
        val tmpPath = Path.of(coverTempDirectory, "$isbn.$extension")
        val coverPath = Path.of(bookCoverDirectory, "$isbn.jpeg")
        val tmpFile = Files.write(tmpPath, image.bytes)
        Thumbnails.of(File(tmpPath.toUri()))
            .size(imageWidth, imageHeight)
            .toFile(File(coverPath.toUri()))
        tmpFile.deleteIfExists()
    }

    override fun fetchAndUploadCover(url: String, isbn: String) {
        val imageUrl = URL(url)
        val inputStream: InputStream = imageUrl.openStream()
        val tmpPath = Path.of(coverTempDirectory, "$isbn.jpeg")
        val coverPath = Path.of(bookCoverDirectory, "$isbn.jpeg")
        val tmpFile = Files.write(tmpPath, inputStream.readBytes())
        Thumbnails.of(File(tmpPath.toUri()))
            .size(imageWidth, imageHeight)
            .toFile(File(coverPath.toUri()))
        tmpFile.deleteIfExists()
    }
}
