import handleErrors from '@/helpers/httpHelper';
import {BasketDto} from "@/models/BasketDto";

class BasketService {

    getBaskets(open: boolean) : Promise<BasketDto[]> {
        return fetch(`/api/basket?${new URLSearchParams({open: open.toString()})}`)
            .then(handleErrors)
            .then(res => res.json())
    }

    closeBasket(basketId: string) : Promise<BasketDto> {
        const requestOptions = {
            method: "DELETE",
        }
        return fetch("/api/basket/" + basketId, requestOptions)
            .then(handleErrors)
            .then(res => res.json())
    }
}

export default new BasketService()