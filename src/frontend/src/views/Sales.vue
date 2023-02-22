<template>
  <h1>Ventes</h1>
  <Message v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{ msg.content }}
  </Message>
  <Dialog v-model:visible="displayViewDialog" header="Aperçu facture" :style="{width: '50w'}">


  </Dialog>
  <div style="width: 15%">
    <VueDatePicker v-model="date" range :preset-ranges="presetRanges" :enable-time-picker="false" :start-time="startTime" @update:model-value="handleDate">
      <template #yearly="{ label, range, presetDateRange }">
        <span @click="presetDateRange(range)">{{ label }}</span>
      </template>
    </VueDatePicker>
  </div>

  <DataTable
      :value="invoices"
      responsive-layout="scroll"
      striped-rows>
    <column field="timeCreated" header="Date" :sortable="true"></column>
    <column field="id" header="ID"></column>
    <column field="quantity" header="Quantité"></column>
    <column field="paymentOption" header="Moyen de paiement" :sortable="true"></column>
    <column field="price" header="Montant"></column>
  </DataTable>
</template>

<script setup>
import {ref} from "vue";
import {useFetchInvoices} from "@/composables/useFetch";
import { endOfMonth, startOfMonth, startOfDay, endOfDay, startOfYear, endOfYear} from 'date-fns';
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
  { label: 'Aujourd\'hui', range: [startOfDay(now), endOfDay(now)] },
  { label: 'Ce mois', range: [startOfMonth(now), endOfMonth(now)] },
  { label: 'Cette année', range: [startOfYear(now), endOfYear(now)] },
]);


const {invoices,messages} = useFetchInvoices(startDate, endDate)


const handleDate = (modelData) => {
  date.value = modelData;
  SaleService.getSales(date.value[0],date.value[1])
      .then((response) => response.json())
      .then((data) => {
        invoices.value = data
      })
      .catch(() => {
        messages.value.push({severity: 'error', content: `Les factures n'ont pas pu être chargés`})
      })
}



let displayViewDialog = ref(false)

</script>

<style scoped>

</style>