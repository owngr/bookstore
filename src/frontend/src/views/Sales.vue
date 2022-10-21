<template>
  <h1>Ventes</h1>
  <Message v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{ msg.content }}
  </Message>
  <Dialog v-model:visible="displayViewDialog" header="Aperçu facture" :style="{width: '50w'}">

  </Dialog>
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

const now = new Date()
const startTime = ref(new Date(now.getFullYear(), now.getMonth(), 1))
const endTime = ref(new Date(now.getFullYear(), now.getMonth() + 1, 0))

let {invoices, messages} = useFetchInvoices(startTime.value, endTime.value)


let displayViewDialog = ref(false)

</script>

<style scoped>

</style>