package ch.wngr.bookstore.controllers


import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/editor")
class EditorController @Autowired constructor(
    private val stockService: StockService,
) {
    @GetMapping()
    fun fetchAllEditors(): List<String> {
        return stockService.getEditors()
    }
}
