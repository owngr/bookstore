package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Distributor
import ch.wngr.bookstore.repositories.DistributorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DistributorServiceImpl @Autowired constructor(
    val distributorRepository: DistributorRepository,
) : DistributorService {

    override fun getOrCreateDistributor(distributor: String): Distributor {
        var existingDistributor: Distributor?
        existingDistributor = distributorRepository.findByName(distributor)
        if (existingDistributor == null) {
            existingDistributor = Distributor(name = distributor)
            existingDistributor = distributorRepository.save(existingDistributor)
        }
        return existingDistributor
    }
}
