import {ref, Ref} from "vue"
import EditorService from "@/service/EditorService"
import DistributorService from "@/service/DistributorService"
import SaleService from "@/service/SaleService";
import i18n from "@/i18n";
import mitt from "mitt";
import {SaleList} from "@/models/SaleDto";

const emitter = mitt()

export function useFetchEditors() : Ref<Editor[]> {
    const editors = ref<Editor[]>([])
    EditorService.getAll()
        .then((response: Response) => response.json())
        .then((data: Editor[]) => {
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

export function useFetchDistributors() : Ref<string[]> {
    const distributors = ref<string[]>([])
    DistributorService.getAll()
        .then((response: Response) => response.json())
        .then((data: string[]) => {
            distributors.value = data
            console.log(data)
        })
        .catch((e) => {
            emitter.emit('notify',{severity: 'error', content: i18n.global.t('couldntLoadDistributorsMessage')})
            console.error(e)
        })
    return distributors
}

export function useFetchInvoices(startTime: Date, endTime: Date) : Ref<SaleList[]> {
    const invoices = ref<SaleList[]>([])
    SaleService.getSales(startTime, endTime)
        .then((response: Response) => response.json())
        .then((data: SaleList[]) => {
            invoices.value = data
            console.debug(data)
        })
        .catch((e) => {
            emitter.emit('notify',{severity: 'error', content: i18n.global.t('couldntLoadInvoicesMessage')})
            console.error(e)
        })
    return invoices
}