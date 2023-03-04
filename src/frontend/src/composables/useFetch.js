import {ref} from "vue"
import EditorService from "@/service/EditorService"
import DistributorService from "@/service/DistributorService"
import SaleService from "@/service/SaleService";
import i18n from "@/i18n";

export function useFetchEditors() {
    const editors = ref([])
    const messages = ref([])
    EditorService.getAll()
        .then((response) => response.json())
        .then((data) => {
            console.log('fetched editors')
            // needded because Drodpown doesn't work with simple list
            editors.value = data
        })
        .catch(() => messages.value.push({severity: 'error', content: i18n.global.t('couldntLoadEditorsMessage')}))
    return {editors, messages }
}

export function useFetchDistributors() {
    const distributors = ref([])
    const messages = ref([])
    DistributorService.getAll()
        .then((response) => response.json())
        .then((data) => {
            distributors.value = data
            console.log(data)
        })
        .catch(() => messages.value.push({severity: 'error', content: i18n.global.t('couldntLoadDistributorsMessage')}))
    return {distributors, messages}
}

export function useFetchInvoices(startTime, endTime) {
    const invoices = ref([])
    const messages = ref([])
    SaleService.getSales(startTime, endTime)
        .then((response) => response.json())
        .then((data) => {
            invoices.value = data
            console.debug(data)
        })
        .catch((e) => {
            messages.value.push({severity: 'error', content: i18n.global.t('couldntLoadInvoicesMessage')})
            console.debug(e)
        })
    return {invoices, messages}
}