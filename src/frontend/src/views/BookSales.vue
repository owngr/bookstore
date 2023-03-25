<template>
  <h1>{{ $t('sales') }}</h1>
  <PMessage v-for="msg of messages" :key="msg.content" :severity="msg.severity" :sticky="false">{{ msg.content }}
  </PMessage>
  <PDialog v-model:visible="displayViewDialog" :header="$t('previewInvoice')" :style="{width: '50w'}">
    <InvoiceDetail :sale-list="invoiceDetail" :style="{width: '50w', height: '50w'}"/>

  </PDialog>
  <div style="width: 30%">
    <VueDatePicker
        v-model="date" :enable-time-picker="false" :preset-ranges="presetRanges" :start-time="startTime"
        range @update:model-value="handleDate">
      <template #yearly="{ label, range, presetDateRange }">
        <span @click="presetDateRange(range)">{{ label }}</span>
      </template>
    </VueDatePicker>
  </div>

  <DataTable
      ref="dt"
      :value="invoices"
      responsive-layout="scroll"
      striped-rows
      @row-click="displayDetailledView"
  >
    <template #header>
      <div class="p-d-flex p-jc-between p-ai-center">
        <button label="Export" @click="exportCSV($event)">{{ $t('export') }}</button>
      </div>
    </template>

    <PColumn :sortable="true" field="timeCreated" header="Date"></PColumn>
    <PColumn field="id" header="ID"></PColumn>
    <PColumn :header="$t('quantity')" field="quantity"></PColumn>
    <PColumn :header="$t('paymentOption')" :sortable="true" field="paymentOption">
      <template #body="slotProps">
        {{ $t(slotProps.data.paymentOption) }}
      </template>
    </PColumn>
    <PColumn :header="$t('price')" field="price"></PColumn>
    <ColumnGroup type="footer">
      <PRow v-for="[key, value] in total" :key="key">
        <PColumn :colspan="4" :footer="$t(key)" style="font-weight: lighter"/>
        <PColumn :colspan="1" :footer="value"/>
      </PRow>
      <PRow>
        <PColumn :colspan="4" :footer="$t('total')"/>
        <PColumn :colspan="1" :footer="totalSum"/>
      </PRow>
    </ColumnGroup>
  </DataTable>
</template>

<script setup>
import {computed, ref} from "vue";
import {useFetchInvoices} from "@/composables/useFetch";
import {endOfDay, endOfMonth, endOfYear, startOfDay, startOfMonth, startOfYear} from 'date-fns';
import SaleService from "@/service/SaleService";
import saleService from "@/service/SaleService";
import i18n from "@/i18n";
import InvoiceDetail from "@/components/InvoiceDetail"

const now = new Date()
const startDate = startOfDay(now)
const endDate = endOfDay(now)

const date = ref([startDate, endDate]);
const startTime = ref([
  {hours: 0, minutes: 0},
  {hours: 23, minutes: 59}]
);

const presetRanges = ref([
  {label: i18n.global.t('today'), range: [startOfDay(now), endOfDay(now)]},
  {label: i18n.global.t('thisMonth'), range: [startOfMonth(now), endOfMonth(now)]},
  {label: i18n.global.t('thisYear'), range: [startOfYear(now), endOfYear(now)]},
]);


const {invoices, messages} = useFetchInvoices(startDate, endDate)

// ref to the datatable
const dt = ref()

let invoiceDetail = ref()


const handleDate = (modelData) => {
  date.value = modelData;
  SaleService.getSales(date.value[0], date.value[1])
      .then((response) => response.json())
      .then((data) => {
        invoices.value = data
      })
      .catch(() => {
        messages.value.push({severity: 'error', content: i18n.global.t('couldntLoadInvoicesMessage')})
      })
}

const exportCSV = () => {
  dt.value.exportCSV();
}

const total = computed(() => {
  const accumulator = new Map()
  accumulator.set('CASH', 0)
  accumulator.set('CARD', 0)
  accumulator.set('TWINT', 0)
  accumulator.set('VOUCHER', 0)
  invoices.value.forEach(invoice => {
    accumulator.set(invoice.paymentOption, Math.round((accumulator.get(invoice.paymentOption) + invoice.price) * 100) / 100)
  })
  console.debug(accumulator)
  return accumulator
})

const totalSum = computed(() => {
  let sum = 0
  total.value.forEach(value => {
    sum = Math.round((sum + value) * 100) / 100
  })
  return sum
})

function displayDetailledView(event) {
  saleService.getInvoice(event.data.id)
      .then((response) => response.json())
      .then((data) => {
        invoiceDetail.value = data
        displayViewDialog.value = true
      })
}

let displayViewDialog = ref(false)

</script>

<style scoped>

</style>