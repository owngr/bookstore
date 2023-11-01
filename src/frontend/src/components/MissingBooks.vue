<template>
  <div style="text-align: center">
    <InlineMessage severity="warn">{{ $t('missingBooksDoYouWishToAddThem') }}</InlineMessage>
    <DataTable
        :value="books">
      <PColumn :header="$t('isbn')" field="isbn"/>
      <PColumn :header="$t('title')" field="title"/>
      <PColumn :header="$t('saleQuantity')" field="saleQuantity"/>
      <PColumn :header="$t('quantityInStock')" field="quantityInStock"/>
    </DataTable>
    <div class="card flex justify-content-center flex-wrap gap-3 pt-4">
      <PButton class="p-button-danger" @click="cancel">{{ $t('cancel') }}</PButton>
      <PButton @click="addMissingBooks">{{ $t('validate') }}</PButton>
    </div>
  </div>

</template>

<script setup>
import {computed, defineProps, defineEmits} from "vue";
import StockService from "@/service/StockService";

const props = defineProps(({
  sales: {
    type: Array,
    default: () => []
  },
  missingBooks: {
    type: Array,
    default: () => []
  }
}))

const emit = defineEmits(['addedBooks']['canceled'])

const books = computed(() => {
  const bookList = []
  props.missingBooks.forEach(mb => {
    const saleQuantity = props.sales
        .filter(s => s.isbn === mb.isbn)
        .reduce((acc, s) => acc + s.quantity, 0)
    console.debug("sale quantity")
    console.debug(mb)
    const book = {
      isbn: mb.isbn,
      title: mb.title,
      saleQuantity: saleQuantity,
      quantityInStock: mb.amount,
    }
    bookList.push(book)
  })
  return bookList
})

function addMissingBooks() {
  const promises = []
  props.missingBooks.forEach(mb => {
    const missingSales = props.sales
        .filter(s => s.isbn === mb.isbn)
    console.debug("missingSales")
    console.debug(missingSales)
    missingSales[0].originalBook.amount = missingSales
        .reduce((acc, s) => acc + s.quantity, 0) - mb.amount
    promises.push(StockService.addBook(missingSales[0].originalBook))
    Promise.all(promises)
        .then(() => {
          emit('addedBooks')
        })
        .catch(e => console.error(e))
  })
}

function cancel() {
  emit('canceled')
}
</script>

<style scoped>

</style>