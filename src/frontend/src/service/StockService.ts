import handleErrors from "@/helpers/httpHelper";

class StockService {

    getStock(params: any) {
        console.debug("lazy params")
        console.debug(params)
        const queryParams = params ? Object.keys(params).map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k])).join('&') : '';
        return fetch("/api/stock?" + queryParams)
            .then(handleErrors)
            .then(res => res.json());
    }

    updateStock(body: ScraperBook) {
        const requestOptions = {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        };
        return fetch("/api/stock", requestOptions)
            .then(handleErrors)
            .then(res => res.json())
    }

    addBook(body: ScraperBook) {
        const requestOptions = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        }
        return fetch("/api/book/stock", requestOptions)
            .then(handleErrors)
    }

    addCover(formData: any, isbn: string) {
        const requestOptions = {
            method: "POST",
            body: formData,
            // headers: {
            //     "Content-Type": "multipart/form-data"
            // }
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

    getMissingBooks(sales: SaleList) {
        const requestOptions = {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(sales)
        };
        return fetch('/api/stock/missing',requestOptions)
    }

    sellBooks(sales: SaleList) {
        const requestOptions = {
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(sales)
        };
        return fetch('/api/stock', requestOptions)
            .then(handleErrors)
    }


}



export default new StockService()
