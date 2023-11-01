import handleErrors from "@/helpers/httpHelper";

class DistributorService {
    getAll() {
        return fetch("/api/distributor")
            .then(handleErrors)
    }
}

export default new DistributorService();