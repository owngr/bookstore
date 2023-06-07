import handleErrors from '@/helpers/httpHelper';

class BasketService {

    getOpenBaskets() {
        return fetch("/api/basket?" + new URLSearchParams({open: true}))
            .then(handleErrors)
            .then(res => res.json())
    }
}

export default new BasketService()