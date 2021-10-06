package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Publisher

interface PublisherService {
    fun getOrCreatePublisher(publisher: String, defaultDistributor: String?): Publisher
}
