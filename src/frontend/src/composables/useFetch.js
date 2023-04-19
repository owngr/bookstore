import {ref} from "vue"
import EditorService from "@/service/EditorService"
import DistributorService from "@/service/DistributorService"
import SaleService from "@/service/SaleService";
import i18n from "@/i18n";
import mitt from "mitt";

const emitter = mitt()

export function useFetchEditors() {
    const editors = ref([])
    EditorService.getAll()
        .then((response) => response.json())
        .then((data) => {
            console.log('fetched editors')
            // needded because Drodpown doesn't work with simple list
            editors.value = data
        })
        .catch((e) => {
            emitter.emit('notify',{severity: 'error', content: i18n.global.t('couldntLoadEditorsMessage')})
            console.error(e)
        })
    return editors
}

export function useFetchDistributors() {
    const distributors = ref([])
    DistributorService.getAll()
        .then((response) => response.json())
        .then((data) => {
            distributors.value = data
            console.log(data)
        })
        .catch((e) => {
            emitter.emit('notify',{severity: 'error', content: i18n.global.t('couldntLoadDistributorsMessage')})
            console.error(e)
        })
    return distributors
}

export function useFetchInvoices(startTime, endTime) {
    const invoices = ref([])
    SaleService.getSales(startTime, endTime)
        .then((response) => response.json())
        .then((data) => {
            invoices.value = data
            console.debug(data)
        })
        .catch((e) => {
            emitter.emit('notify',{severity: 'error', content: i18n.global.t('couldntLoadInvoicesMessage')})
            console.error(e)
        })
    return invoices
}