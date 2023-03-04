<template>
  <h1>Ventes</h1>
  <Message v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{ msg.content }}
  </Message>
  <Dialog v-model:visible="displayViewDialog" header="Aperçu facture" :style="{width: '50w'}">


  </Dialog>
  <div style="width: 30%">
    <VueDatePicker v-model="date" :enable-time-picker="false" :preset-ranges="presetRanges" :start-time="startTime"
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
  >
    <template #header>
      <div class="p-d-flex p-jc-between p-ai-center">
        <button label="Export" @click="exportCSV($event)">Exporter</button>
      </div>
    </template>
    <column field="timeCreated" header="Date" :sortable="true"></column>
    <column field="id" header="ID"></column>
    <column field="quantity" header="Quantité"></column>
    <column field="paymentOption" header="Moyen de paiement" :sortable="true"></column>
    <column field="price" header="Montant"></column>
    <ColumnGroup type="footer">
      <Row v-for="[key, value] in total" :key="key">
        <Column :colspan="4" :footer="key" style="font-weight: lighter"/>
        <Column :colspan="1" :footer="value"/>
      </Row>
      <Row>
        <Column :colspan="4" footer="Total"/>
        <Column :colspan="1" :footer="totalSum"/>
      </Row>
    </ColumnGroup>
  </DataTable>
</template>

<script setup>
import {computed, ref} from "vue";
import {useFetchInvoices} from "@/composables/useFetch";
import {endOfDay, endOfMonth, endOfYear, startOfDay, startOfMonth, startOfYear} from 'date-fns';
import SaleService from "@/service/SaleService";

const now = new Date()
const startDate = startOfDay(now)
const endDate = endOfDay(now)

const date = ref([startDate, endDate]);
const startTime = ref([
    { hours: 0, minutes: 0 },
  {hours: 23, minutes: 59 }]
);

const presetRanges = ref([
  {label: 'Aujourd\'hui', range: [startOfDay(now), endOfDay(now)]},
  {label: 'Ce mois', range: [startOfMonth(now), endOfMonth(now)]},
  {label: 'Cette année', range: [startOfYear(now), endOfYear(now)]},
]);


const {invoices, messages} = useFetchInvoices(startDate, endDate)

// ref to the datatable
const dt = ref()


const handleDate = (modelData) => {
  date.value = modelData;
  SaleService.getSales(date.value[0], date.value[1])
      .then((response) => response.json())
      .then((data) => {
        invoices.value = data
      })
      .catch(() => {
        messages.value.push({severity: 'error', content: `Les factures n'ont pas pu être chargés`})
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

let displayViewDialog = ref(false)

</script>

<style scoped>

</style>