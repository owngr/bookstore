package ch.wngr.bookstore.services

import ch.wngr.bookstore.repositories.BookRepository
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfInt
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs.*
import org.opencv.imgproc.Imgproc.resize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists

@Service
class CoverServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
) : CoverService {

    @Value("\${books.cover.directory:covers}")
    lateinit var bookCoverDirectory: String

    @Value("\${books.cover.tempDirectory:tmp}")
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
        TODO("Not yet implemented")
    }

    private fun convertImage(image: MultipartFile, isbn: String) {
        val extension = image.name.substringAfterLast(".", "")
        val tmpPath = Path.of(coverTempDirectory, "$isbn.$extension")
        val coverPath = Path.of(bookCoverDirectory, "$isbn.jpeg")
        val tmpFile = Files.write(tmpPath, image.bytes)
        val originalImage = imread(tmpPath.toString())
        val resizedImage = Mat()
        val convertedImage = Mat.zeros(
            imageHeight,
            imageWidth,
            CvType.CV_8UC1
        )

        if (originalImage.size().height > originalImage.size().width) {
            resize(
                originalImage, resizedImage,
                Size(
                    originalImage.size().width / (originalImage.size().height / imageHeight),
                    imageHeight.toDouble()
                )
            )
        } else {
            resize(
                originalImage, resizedImage,
                Size(
                    imageWidth.toDouble(),
                    originalImage.size().height / (originalImage.size().width / imageWidth),
                )
            )
        }
        val rowStart = (imageWidth - resizedImage.size().width) / 2
        val rowEnd = rowStart + resizedImage.size().width
        val colStart = (imageHeight - resizedImage.size().height) / 2
        val colEnd = colStart + resizedImage.size().height
        resizedImage.copyTo(
            convertedImage.rowRange(rowStart.toInt(), rowEnd.toInt()),
            convertedImage.colRange(colStart.toInt(), colEnd.toInt()),
        )
        imwrite(coverPath.toString(), convertedImage, MatOfInt(IMWRITE_JPEG_QUALITY, 80))
        // cleaning up
        tmpFile.deleteIfExists()
    }
}
