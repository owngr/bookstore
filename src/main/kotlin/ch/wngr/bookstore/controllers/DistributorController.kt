package ch.wngr.bookstore.controllers


import ch.wngr.bookstore.models.Editor
import ch.wngr.bookstore.services.DistributorService
import ch.wngr.bookstore.services.PublisherService
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/distributor")
class DistributorController @Autowired constructor(
    private val distributorService: DistributorService
) {
    @GetMapping()
    fun fetchAllPublisher(): List<String> {
        return distributorService.getDistributors()
    }
}
