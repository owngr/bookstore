package ch.wngr.bookstore.services

import org.springframework.web.multipart.MultipartFile

interface CoverService {

    fun uploadBookCover(image: MultipartFile, isbn: String)
    fun downloadBookCover(isbn: String): ByteArray
}
