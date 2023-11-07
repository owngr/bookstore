import handleErrors from "@/helpers/httpHelper";

class DistributorService {
  getAll(): Promise<Response> {
    return fetch("/api/distributor").then(handleErrors);
  }
}

export default new DistributorService();
