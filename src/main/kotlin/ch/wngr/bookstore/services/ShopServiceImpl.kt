package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toShopEntry
import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.ShopEntry
import ch.wngr.bookstore.repositories.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShopServiceImpl @Autowired constructor(
    val stockRepository: StockRepository,
) : ShopService {

    override fun getShopEntries(): List<ShopEntry> {
        val shopEntryList: MutableList<ShopEntry> = ArrayList()
        var shopEntry: ShopEntry
        val stock: List<Stock> = stockRepository.findByBook_HasCover(true)
        for (stockEnt: Stock in stock) {
            shopEntry = stockEnt.toShopEntry()
            shopEntryList.add(shopEntry)
        }
        return shopEntryList
    }
}