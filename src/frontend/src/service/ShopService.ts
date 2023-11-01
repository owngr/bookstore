import handleErrors from "@/helpers/httpHelper";

class ShopService {

    getShop() {
        return fetch("/api/shop")
            .then(handleErrors)
            .then(res => res.json())
    }

    getPicture(isbn: string) {
        return fetch("/api/shop/" + isbn)
            .then(handleErrors)
    }
}

export default new ShopService()