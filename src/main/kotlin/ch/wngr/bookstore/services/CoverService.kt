package ch.wngr.bookstore.services

import org.springframework.web.multipart.MultipartFile

interface CoverService {

    fun uploadBookCover(image: MultipartFile)
    fun downloadBookCover(isbn: String): ByteArray
}
