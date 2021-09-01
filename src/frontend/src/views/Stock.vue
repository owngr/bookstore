<template>
  <h1>Stock</h1>
  <div>
    <DataTable
        :value="stockEntries"
        responsiveLayout="scroll"
        ref="dt" csv-separator=";"
        stripedRows
         :globalFilterFields="['isbn','title','authors','editor']"
        v-model:filters="filters"
    >
      <template #header>
        <div class="p-d-flex p-jc-between p-ai-center">
          <button  label="Export" @click="exportCSV($event)">Exporter</button>
          <span class="p-input-icon-left">
                                <i class="pi pi-search" />
                                <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
                            </span>
        </div>
      </template>
      <Column field="isbn" header="ISBN"></Column>
      <Column field="title" sortable="true" header="Titre"></Column>
      <Column field="authors" header="Auteur·rices">
        <template #body="slotProps">
          <li v-for="author in slotProps.data.authors" v-bind:key="author">
            {{ author }}
          </li>
        </template>
      </Column>
      <Column field="editor" header="Maison d'édition"></Column>
      <Column field="description" header="Résumé" hidden="true"></Column>
      <Column field="amount" header="Quantité" :exportable="false"></Column>
    </DataTable>
  </div>
</template>

<script>
// import { ref, onMounted } from 'vue';
import StockService from '../service/StockService';
import {FilterMatchMode} from 'primevue/api';


export default {
  name: "Stock",
  el: '#app',
  data() {
    return {
      stockEntries: null,
      filters: {
        'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
      },
    }
  },
  stockService: null,
  created() {
    this.stockService = new StockService();
  },
  mounted() {
    this.stockService.getStock().then(data => {
      console.log("stockService finished")
      console.log(data)
      this.stockEntries = data}
    );
  },
  methods: {
    exportCSV() {
      this.$refs.dt.exportCSV();
    }
  }
}
</script>