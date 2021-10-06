package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.repositories.PublisherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PublisherServiceImpl @Autowired constructor(
    val publisherRepository: PublisherRepository,
    val distributorService: DistributorService,
) : PublisherService {
    override fun getOrCreatePublisher(publisher: String, defaultDistributor: String?): Publisher {
        var existingPublisher: Publisher?
        existingPublisher = publisherRepository.findByName(publisher)
        if (existingPublisher == null) {
            val distributor = defaultDistributor?.let { distributorService.getOrCreateDistributor(it) }
            existingPublisher = Publisher(name = publisher, defaultDistributor = distributor)
            existingPublisher = publisherRepository.save(existingPublisher)
        }
        return existingPublisher
    }


}
