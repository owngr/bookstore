import handleErrors from '@/helpers/httpHelper';

class BasketService {

    getBaskets(open) {
        return fetch("/api/basket?" + new URLSearchParams({open: open}))
            .then(handleErrors)
            .then(res => res.json())
    }

    closeBasket(basketId) {
        const requestOptions = {
            method: "DELETE",
        }
        return fetch("/api/basket/" + basketId, requestOptions)
            .then(handleErrors)
            .then(res => res.json())
    }
}

export default new BasketService()