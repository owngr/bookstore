<template>
  <h1>Stock</h1>
  <div>
    <DataTable :value="stockEntries" responsiveLayout="scroll" ref="dt" csv-separator=";" stripedRows>
      <template #header>
        <div style="text-align: left">
          <button  label="Export" @click="exportCSV($event)">Exporter</button>
        </div>
      </template>
      <Column field="isbn" header="ISBN"></Column>
      <Column field="title" header="Titre"></Column>
      <Column field="authors" header="Auteur·rices">
        <template #body="slotProps">
          <li v-for="author in slotProps.data.authors" v-bind:key="author">
            {{ author }}
          </li>
        </template>
      </Column>
      <Column field="editor" header="Maison d'édition"></Column>
      <Column field="amount" header="Quantité"></Column>
    </DataTable>
  </div>
</template>

<script>
// import { ref, onMounted } from 'vue';
import StockService from '../service/StockService';


export default {
  name: "Stock",
  el: '#app',
  data() {
    return {
      stockEntries: null
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