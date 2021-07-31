
export default class StockService {

    getStock() {
        return fetch("/api/stock")
            .then(res => res.json());
    }
}
