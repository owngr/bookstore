package ch.wngr.bookstore.integration

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.TagDto
import ch.wngr.bookstore.services.StockService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = ["classpath:application-test.properties"]
)
class StockControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val stockService: StockService,
) {
    //TODO find a way to compare JSON content
    @Test
    fun getStockReturnExistingBook() {
        val scraperBook = ScraperBook(
            isbn = "123456789102",
            title = "Test book",
            authors = arrayListOf("Joy", "Purro"),
            editor = "10x18",
            distributor = "OLF",
            description = "A test book",
            price = 10.5f,
            hasCover = false,
            amount = 1,
            tags = arrayListOf(TagDto(0, true, "féminisme"), TagDto(1, false, "roman"))
        )
        val pageable: Pageable = PageRequest.of(0,50)
        val scrapperPage: Page<ScraperBook> = PageImpl(listOf(scraperBook), pageable, 100)
        stockService.addBook(scraperBook)

        val criterion = "{\"first\":0,\"rows\":50,\"sortField\":\"amount\",\"sortOrder\":null,\"filters\":{\"global\":{\"value\":\"123456789102\",\"matchMode\":\"contains\"},\"tags\":{\"value\":[],\"matchMode\":\"contains\"},\"displayEmptyEntries\":{\"value\":false,\"matchMode\":\"contains\"}}}"
        mvc.perform(
            MockMvcRequestBuilders.get("/api/stock?lazyEvent={criterion}", criterion)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
    }
}