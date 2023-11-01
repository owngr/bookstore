import handleErrors from '@/helpers/httpHelper';

class BasketService {

    getBaskets(open: boolean) {
        return fetch(`/api/basket?${new URLSearchParams({open: open.toString()})}`)
            .then(handleErrors)
            .then(res => res.json())
    }

    closeBasket(basketId: string) {
        const requestOptions = {
            method: "DELETE",
        }
        return fetch("/api/basket/" + basketId, requestOptions)
            .then(handleErrors)
            .then(res => res.json())
    }
}

export default new BasketService()