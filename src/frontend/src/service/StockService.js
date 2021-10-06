
class StockService {

    getStock() {
        return fetch("/api/stock")
            .then(res => res.json());
    }

    updateStock(body) {
        const requestOptions = {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        };
        return fetch("/api/stock", requestOptions)
            .then(res => res.json())
    }
}

export default new StockService()
