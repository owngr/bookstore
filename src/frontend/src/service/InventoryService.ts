import handleErrors from "@/helpers/httpHelper";

class InventoryService {

    getInventory() {
        return fetch("/api/inventory")
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
            body: formData
        }
        return fetch("/api/book/cover?" + new URLSearchParams({ isbn } ), requestOptions)
            .then(handleErrors)
    }


}



export default new InventoryService()
