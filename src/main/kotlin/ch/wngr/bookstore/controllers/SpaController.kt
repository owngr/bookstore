package ch.wngr.bookstore.controllers

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse


@RestController
class SpaController {

    @GetMapping("/index.html")
    fun getIndex(response: HttpServletResponse) {
        println("In getIndex")
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
    }

    @GetMapping("/")
    fun getIndexRoot(response: HttpServletResponse) {
        println("In getIndexRoot")
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
    }
}