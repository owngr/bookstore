package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Distributor

interface DistributorService {
    fun getOrCreateDistributor(distributor: String): Distributor
    fun getDistributors(): List<String>
}