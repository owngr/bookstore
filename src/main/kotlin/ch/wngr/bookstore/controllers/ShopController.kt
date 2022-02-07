package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.ShopEntry
import ch.wngr.bookstore.services.CoverService
import ch.wngr.bookstore.services.ShopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.NullPointerException

@RestController
@RequestMapping("/api/shop")
class ShopController @Autowired constructor(
    private val shopService: ShopService,
    private val coverService: CoverService,
){

    @GetMapping("")
    fun getShopEntries(): List<ShopEntry> {
        return shopService.getShopEntries()
    }

    @GetMapping("/{isbn}")
    fun getCoverByIsbn(@PathVariable("isbn") isbn: String): ResponseEntity<Any> {
        return try {
            val cover = coverService.downloadBookCover(isbn)
            ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$isbn.jpeg\"")
                .body(cover)
        } catch (error: NullPointerException) {
            ResponseEntity
                .notFound()
                .build()
        }
    }
}