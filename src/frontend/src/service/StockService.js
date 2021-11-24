import handleErrors from "@/helpers/httpHelper";

class StockService {

    getStock() {
        return fetch("/api/stock")
            .then(handleErrors)
            .then(res => res.json());
    }

    updateStock(body) {
        const requestOptions = {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        };
        return fetch("/api/stock", requestOptions)
            .then(handleErrors)
            .then(res => res.json())
    }

    addBook(body) {
        const requestOptions = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        }
        return fetch("/api/book/stock", requestOptions)
            .then(handleErrors)
    }

    addCover(formData, isbn) {
        const requestOptions = {
            method: "POST",
            body: formData
        }
        return fetch("/api/book/cover?" + new URLSearchParams({ isbn } ), requestOptions)
            .then(handleErrors)
    }


}



export default new StockService()
