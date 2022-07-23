package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toShopEntry
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.ShopEntry
import ch.wngr.bookstore.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShopServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
) : ShopService {

    override fun getShopEntries(): List<ShopEntry> {
        val shopEntryList: MutableList<ShopEntry> = ArrayList()
        var shopEntry: ShopEntry
        val stock: List<Book> = bookRepository.findByHasCover(true)
        for (stockEnt: Book in stock) {
            shopEntry = stockEnt.toShopEntry()
            shopEntryList.add(shopEntry)
        }
        return shopEntryList
    }
}