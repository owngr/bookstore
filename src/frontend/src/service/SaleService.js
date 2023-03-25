import handleErrors from "@/helpers/httpHelper";

class SaleService {

    getSales(startTime, endTime) {
        console.debug(startTime)
        console.debug(endTime)
        return fetch("/api/sales?" + new URLSearchParams({startTime: startTime.toISOString(), endTime: endTime.toISOString()}))
            .then(handleErrors)
    }

    getInvoice(invoiceId) {
        return fetch("api/invoices/" + invoiceId.toString())
            .then(handleErrors)
    }

}

export default new SaleService()