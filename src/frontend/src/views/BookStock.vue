<template>
  <h1>{{ $t("stock") }}</h1>
  <div>
    <PDialog
      v-model:visible="displayEditDialog"
      :header="$t('updateStockEntry')"
      :style="{ width: '56rem' }"
    >
      <StockEntryEdit
        v-model:book="book"
        :edit-mode="true"
        :process-form-function="updateStock"
        :submit-button-text="$t('updateBook')"
        @close-dialog="closeEditDialog"
      />
    </PDialog>
    <DataTable
      ref="dt"
      :global-filter-fields="['isbn', 'title', 'authors', 'editor']"
      :lasy="true"
      :paginator="true"
      :rows="lazyParams.rows"
      :rows-per-page-options="[50, 100, 200]"
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
            <i class="pi pi-search" />
            <InputText
              v-model="filters['global'].value"
              :placeholder="$t('search')"
              @keydown.enter="onFilter()"
            />
          </span>
          <AutoComplete
            id="tags"
            v-model="filters['tags'].value"
            :placeholder="$t('tags')"
            :suggestions="tagsSuggestions"
            dropdown
            multiple
            name="tags"
            option-label="name"
            @complete="searchTags"
            @keydown.enter="onFilter()"
          />
          <label class="ml-2" for="displayEmptyEntries">{{ $t('displayEmptyEntries') }}</label>
          <InputSwitch v-model="filters['displayEmptyEntries'].value" input-id="displayEmptyEntries" @input="onFilter()"/>
        </div>
      </template>
      <PColumn field="isbn" header="ISBN"></PColumn>
      <PColumn :header="$t('title')" :sortable="true" field="title">
        <template #body="slotProps">
          <a @click="openEditDialog(slotProps.data)">{{
            slotProps.data.title
          }}</a>
        </template>
      </PColumn>
      <PColumn :header="$t('authors')" field="authors">
        <template #body="slotProps">
          <li v-for="author in slotProps.data.authors" :key="author">
            {{ author }}
          </li>
        </template>
      </PColumn>
      <PColumn :header="$t('editor')" field="editor"></PColumn>
      <PColumn
        :header="$t('summary')"
        :hidden="true"
        field="description"
      ></PColumn>
      <PColumn
        :exportable="false"
        :header="$t('quantity')"
        :sortable="true"
        field="amount"
      ></PColumn>
      <PColumn
        :exportable="false"
        :header="$t('cover')"
        :sortable="true"
        field="hasCover"
      >
        <template #body="slotProps">
          {{ $t(slotProps.data.hasCover.toString()) }}
        </template>
      </PColumn>
      <template #footer>
        <div class="flex justify-content-center flex-wrap">
          <label class="form-label m-2" for="paginatorDropdown">{{
            $t("showingFromToOn", {
              start: offset,
              end: offset + pageSize,
              total: totalRecords,
            })
          }}</label>
          <DropDown
            id="paginatorDropdown"
            v-model="currentPage"
            :options="pages"
            class="m-2"
            placeholder="1"
            @change="onPageChange($event)"
          />
          <label class="form-label m-2" for="rowDropdown">{{
            $t("numberOfItemsPerPage")
          }}</label>
          <DropDown
            id="rowDropdown"
            v-model="lazyParams.rows"
            :options="[50, 100, 200]"
            class="m-2"
            placeholder="50"
            @change="onRowsPerPage()"
          />
        </div>
      </template>
    </DataTable>
  </div>
</template>

<script setup>
import StockService from "../service/StockService";
import { FilterMatchMode } from "primevue/api";
import StockEntryEdit from "@/components/StockEntryEdit";
import { inject, ref } from "vue";
import tagService from "@/service/TagService";
import i18n from "@/i18n";

const stockEntries = ref(null);
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  tags: { value: [], matchMode: FilterMatchMode.CONTAINS },
  displayEmptyEntries: { value: false, matchMode: FilterMatchMode.CONTAINS },
});
const displayEditDialog = ref(false);
const book = ref({});
let totalRecords = ref(0);
const pages = ref([1]);
let currentPage = ref(1);
let offset = ref(0);
let pageSize = ref(50);
const lazyParams = ref({
  first: 0,
  rows: 50,
  sortField: "amount",
  sortOrder: null,
  filters: filters,
});
let exportContent = ref(null);
// ref to the datatable
const dt = ref();

const tags = ref([]);
const tagsSuggestions = ref([]);

const emitter = inject("emitter");
// lazyParams.value.rows = dt.value.rows
fetchStock(lazyParams);

loadTags();

function loadTags() {
  console.debug("loading tags");
  tagService
    .getTags()
    .then((data) => {
      tags.value = data;
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("couldNotLoadTagsMessage", { error: e }),
      })
    );
}

function fetchStock(lazyParams, loadIntoTable = true) {
  delete lazyParams.value.filteredValue;
  return StockService.getStock({
    lazyEvent: JSON.stringify(lazyParams.value),
  }).then((data) => {
    if (loadIntoTable) {
      stockEntries.value = data.content;
      totalRecords.value = data.totalElements;
      offset.value = data.pageable.offset;
      pageSize.value = data.numberOfElements;
      createPageRange(data.totalPages);
    } else {
      return data.content;
    }
  });
}

function createPageRange(totalPages) {
  console.debug("total pages");
  console.debug(totalPages);
  // create a range from [1.. totalPages]
  pages.value = Array.from({ length: totalPages }, (v, k) => k + 1);
  console.debug(pages);
}

function exportCSV() {
  const oldRowCount = lazyParams.value.rows;
  const oldFirst = lazyParams.value.first;
  lazyParams.value.rows = 10000;
  lazyParams.value.first = 0;
  fetchStock(lazyParams, false).then((data) => {
    console.debug("in fetchstock");
    console.debug(data);
    dt.value.exportCSV(null, data);
    lazyParams.value.rows = oldRowCount;
    lazyParams.value.first = oldFirst;
  });
  console.debug("export content");
  console.debug(exportContent.value);
}

function openEditDialog(bookToOpen) {
  book.value = bookToOpen;
  displayEditDialog.value = true;
}

function closeEditDialog() {
  displayEditDialog.value = false;
}

function updateStock(childBook) {
  const body = {
    isbn: childBook.isbn,
    title: childBook.title,
    authors: childBook.authors.map((a) => a.value),
    editor: childBook.editor,
    distributor: childBook.distributor,
    description: childBook.description,
    price: childBook.price,
    coverUrl: childBook.coverUrl,
    hasCover: childBook.hasCover,
    amount: childBook.amount,
    tags: childBook.tags,
  };
  closeEditDialog();
  return StockService.updateStock(body).then(fetchStock(lazyParams));
}

function onPageChange(event) {
  console.debug("on page");
  console.debug(event);
  lazyParams.value.first = event.value - 1;
  fetchStock(lazyParams);
}

function onRowsPerPage() {
  currentPage.value = 1;
  lazyParams.value.first = currentPage.value - 1;
  fetchStock(lazyParams);
}

function onSort(event) {
  console.debug("on sort");
  console.debug(event);
  currentPage.value = 1;
  lazyParams.value.first = currentPage.value - 1;
  lazyParams.value.sortField = event.sortField;
  lazyParams.value.sortOrder = event.sortOrder;
  fetchStock(lazyParams);
}

function onFilter() {
  currentPage.value = 1;
  lazyParams.value.first = currentPage.value - 1;
  fetchStock(lazyParams);
}

const bookContainTag = (tag) => {
  for (const t of filters.value["tags"].value) {
    if (t.id === tag.id) {
      return true;
    }
  }
  return false;
};
const searchTags = (event) => {
  console.debug(event.query);
  tagsSuggestions.value = tags.value.filter((tag) => {
    return (
      tag.name.toLowerCase().startsWith(event.query.toLowerCase()) &&
      !bookContainTag(tag)
    );
  });
};
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
