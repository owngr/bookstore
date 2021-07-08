package ch.wngr.bookstore

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages")
class MessageController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Full Stack Java with Springboot and vuejs";
    }
}