package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ShopEntry
import org.springframework.stereotype.Service

@Service
interface ShopService {
    fun getShopEntries(): List<ShopEntry>
}
