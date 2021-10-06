<template>
  <h1>Stock</h1>
  <div>
    <Dialog header="Mise à jour entrée stock" v-model:visible="displayEditDialog" :style="{width: '50vw'}">
      <StockEntryEdit v-model:book="book" @close-dialog="closeEditDialog"/>
    </Dialog>
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
      <Column field="title" :sortable="true" header="Titre">
        <template #body="slotProps">
          <a @click="openEditDialog(slotProps.data)">{{slotProps.data.title}}</a>
        </template>
      </Column>
      <Column field="authors" header="Auteur·rices">
        <template #body="slotProps">
          <li v-for="author in slotProps.data.authors" v-bind:key="author">
            {{ author }}
          </li>
        </template>
      </Column>
      <Column field="editor" header="Maison d'édition"></Column>
      <Column field="description" header="Résumé" :hidden="true"></Column>
      <Column field="amount" header="Quantité" :exportable="false"></Column>
    </DataTable>
  </div>
</template>

<script>
// import { ref, onMounted } from 'vue';
import StockService from '../service/StockService';
import {FilterMatchMode} from 'primevue/api';
import StockEntryEdit from "@/components/StockEntryEdit";


export default {
  name: "Stock",
  components: {
    StockEntryEdit
  },
  el: '#app',
  data() {
    return {
      stockEntries: null,
      filters: {
        'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
      },
      displayEditDialog: false,
      book: {}
    }
  },

  mounted() {
    StockService.getStock().then(data => {
      this.stockEntries = data}
    );
  },
  methods: {
    exportCSV() {
      this.$refs.dt.exportCSV();
    },
    openEditDialog(book) {
      this.book = book
      this.displayEditDialog = true;
    },
    closeEditDialog() {
      this.displayEditDialog = false;
    }
  }
}
</script>