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
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = ["classpath:application-test.properties"]
)
class BookControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val stockService: StockService,
) {
    @Test
    fun fetchBookInfoReturnExistingBook() {
        val scraperBook = ScraperBook(
            isbn = "123456789101",
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
        stockService.addBook(scraperBook)

        mvc.perform(
            get("/api/book/ISBN?isbn=123456789101")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(
                content().json(Json.encodeToString(scraperBook))
            )
    }
}