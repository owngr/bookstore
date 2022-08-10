package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.SaleList
import org.springframework.http.ResponseEntity

interface SaleService {

    fun sellBooks(saleList: SaleList): ResponseEntity<SaleList>
}