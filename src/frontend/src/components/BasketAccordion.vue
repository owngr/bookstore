<template>
  <PButton @click="exportCSV">{{ $t('export') }}</PButton>

  <table>
    <tr>
      <th>{{ $t('isbn') }}</th>
      <th>{{ $t('title') }}</th>
      <th>{{ $t('quantity') }}</th>
    </tr>
    <tr v-for="book in props.basket.books" :key="book.isbn">
      <td>{{ book.isbn }}</td>
      <td>{{ book.title }}</td>
      <td>{{ book.quantity }}</td>
    </tr>

  </table>
</template>

<script setup>
import {defineProps, ref} from 'vue';

const props = defineProps({
  basket: {
    type: Object,
    default: () => {
    }
  },
})


const books = ref(props.basket.books)


function exportCSV() {
  let file = "ISBN,Titre,Quantité"
  books.value.forEach(b => {
    file += `\n ${b.isbn},"${b.title}",${b.quantity}`
  })
  const anchor = document.createElement('a');
  anchor.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(file);
  anchor.target = '_blank';
  anchor.download = props.basket.title + '.csv';
  anchor.click()
}
</script>

<style scoped>

th, td {
  text-align: left;
  padding: 8px;
}

tr:nth-child(odd) {
  background-color: #F8F9FA;
}

td {
  border-top: 1px solid #ddd;
}

th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #F8F9FA;
}

table {
  width: 100%;
  border: 1px solid #ddd;
}

</style>