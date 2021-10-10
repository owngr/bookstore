
class DistributorService {
    getAll() {
        return fetch("/api/distributor")
    }
}

export default new DistributorService();