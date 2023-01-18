<template>
  <h1>Stock</h1>
  <div>
    <Dialog v-model:visible="displayEditDialog" :style="{width: '50vw'}" header="Mise à jour entrée stock">
      <StockEntryEdit
          v-model:book="book"
          :edit-mode="true"
          :process-form-function="updateStock"
          submit-button-text="Mettre à jour le livre"
          @close-dialog="closeEditDialog"
      />
    </Dialog>
    <DataTable
        ref="dt"
        :global-filter-fields="['isbn','title','authors','editor']" :lasy="true"
        :paginator="true"
        :rows="lazyParams.rows"
        :rows-per-page-options="[50,100,200]"
        :total-records="totalRecords"
        :value="stockEntries"
        csv-separator=";"
        current-page-report-template="Showing {first} to {last} of {totalRecords}"
        paginator-template=""
        responsive-layout="scroll"
        striped-rows
        @page="onPageChange($event)"
        @sort="onSort($event)"
    >
      <template #header>
        <div class="p-d-flex p-jc-between p-ai-center">
          <button label="Export" @click="exportCSV($event)">Exporter</button>

          <span class="p-input-icon-left">
                                <i class="pi pi-search"/>
                                <InputText v-model="filters['global'].value" placeholder="Keyword Search"
                                           @keydown.enter="onFilter()"/>
                            </span>
        </div>
      </template>
      <Column field="isbn" header="ISBN"></Column>
      <Column :sortable="true" field="title" header="Titre">
        <template #body="slotProps">
          <a @click="openEditDialog(slotProps.data)">{{ slotProps.data.title }}</a>
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
      <Column :hidden="true" field="description" header="Résumé"></Column>
      <Column :exportable="false" field="amount" header="Quantité"></Column>
      <Column :exportable="false" :sortable="true" field="hasCover" header="Couverture"></Column>
      <template #footer>
        <div class="flex justify-content-center flex-wrap ">
          <label class="form-label m-2" for="paginatorDropdown">Affichage de {{ offset }} à {{ offset + pageSize }} sur
            {{ totalRecords }}</label>
          <Dropdown id="paginatorDropdown" v-model="currentPage" :options="pages" placeholder=1 class="m-2"
                    @change="onPageChange($event)"/>
          <label class="form-label m-2" for="rowDropdown">Nombre d'éléments par page</label>
          <Dropdown id="rowDropdown" v-model="lazyParams.rows" :options="[50,100,200]" class="m-2" placeholder=50
                    @change="onRowsPerPage()"/>
        </div>
      </template>
    </DataTable>
  </div>
</template>

<script>
import StockService from '../service/StockService';
import {FilterMatchMode} from 'primevue/api';
import StockEntryEdit from "@/components/StockEntryEdit";


export default {
  el: '#app',
  name: "Stock",
  components: {
    StockEntryEdit
  },
  data() {
    return {
      stockEntries: null,
      filters: {
        'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
      },
      displayEditDialog: false,
      book: {},
      totalRecords: 0,
      pages: [1],
      currentPage: 1,
      offset: 0,
      pageSize: 50,
      lazyParams: {
        first: 0,
        rows: 50,
        sortField: "amount",
        sortOrder: null,
        filters: null,
      },
      exportContent: null
    }
  },

  mounted() {
    console.debug("mounted")
    this.lazyParams.rows = this.$refs.dt.rows;
    this.lazyParams.filters = this.filters;
    this.fetchStock(this.lazyParams);
  },
  methods: {
    fetchStock(lazyParams, loadIntoTable = true) {
      console.debug("filtered values")
      console.debug(lazyParams)
      delete lazyParams.filteredValue
      return StockService.getStock({lazyEvent: JSON.stringify(lazyParams)}).then(data => {
        if (loadIntoTable) {
          this.stockEntries = data.content
          this.totalRecords = data.totalElements
          this.offset = data.pageable.offset
          this.pageSize = data.numberOfElements
          this.createPageRange(data.totalPages)
        } else {
          return data.content
        }
      });
    },
    createPageRange(totalPages) {
      console.debug("total pages")
      console.debug(totalPages)
      // create a range from [1.. totalPages]
      this.pages = Array.from({length: totalPages}, (v, k) => k + 1)
      console.debug(this.pages)
    },
    exportCSV() {
      const oldRowCount = this.lazyParams.rows
      const oldFirst = this.lazyParams.first
      this.lazyParams.rows = 10000
      this.lazyParams.first = 0
      this.fetchStock(this.lazyParams, false).then(data => {
        console.debug("in fetchstock")
        console.debug(data)
        this.$refs.dt.exportCSV(null, data);
        this.lazyParams.rows = oldRowCount
        this.lazyParams.first = oldFirst
      })
      console.debug("export content")
      console.log(this.exportContent)
    },
    openEditDialog(book) {
      this.book = book
      this.displayEditDialog = true;
    },
    closeEditDialog() {
      this.displayEditDialog = false;
    },
    updateStock(book) {
      const body = {
        isbn: book.isbn,
        title: book.title,
        authors: book.authors.map(a => a.value),
        editor: book.editor,
        distributor: book.distributor,
        description: book.description,
        price: book.price,
        amount: book.amount,
      }
      this.closeEditDialog();
      return StockService.updateStock(body)
          .then(this.fetchStock)
    },
    onPageChange(event) {
      console.debug("on page")
      console.debug(event)
      this.lazyParams.first = event.value - 1
      this.fetchStock(this.lazyParams);
    },
    onRowsPerPage() {
      this.currentPage = 1
      this.lazyParams.first = this.currentPage - 1
      this.fetchStock(this.lazyParams)
    },
    onSort(event) {
      console.debug("on sort")
      console.debug(event)
      this.currentPage = 1
      this.lazyParams.first = this.currentPage - 1
      this.lazyParams.sortField = event.sortField;
      this.lazyParams.sortOrder = event.sortOrder;
      this.fetchStock(this.lazyParams);
    },
    onFilter() {
      this.currentPage = 1
      this.lazyParams.first = this.currentPage - 1
      this.fetchStock(this.lazyParams)
    }
  }
}
</script>
<style scoped>
.p-dropdown {
  width: 5rem;
}

div >>> .p-hidden-accessible {
  border: 0;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}
</style>