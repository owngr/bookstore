import handleErrors from "@/helpers/httpHelper";

class StockService {

    getStock(params) {
        console.debug("lazy params")
        console.debug(params)
        const queryParams = params ? Object.keys(params).map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k])).join('&') : '';
        return fetch("/api/stock?" + queryParams)
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

    deleteStock() {
        const requestOptions = {
            method: "DELETE",
        }
        return fetch("/api/stock", requestOptions)
            .then(handleErrors)
    }

    getMissingBooks(sales) {
        const requestOptions = {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(sales)
        };
        return fetch('/api/stock/missing',requestOptions)
    }

    sellBooks(sales) {
        const requestOptions = {
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(sales)
        };
        return fetch('/api/stock', requestOptions)
    }


}



export default new StockService()
