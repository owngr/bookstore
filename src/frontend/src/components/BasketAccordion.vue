<template>
  <div class="card flex flex-wrap gap-2 p-1">
    <PButton @click="exportCSV">{{ $t('export') }}</PButton>
    <PButton v-if="displayCloseButton" class="p-button-danger" @click="closeBasket">{{ $t('closeBasket') }}</PButton>
  </div>

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
import {defineEmits, defineProps, ref} from 'vue';

const props = defineProps({
  basket: {
    type: Object,
    default: () => {
    }
  },
})

const emit = defineEmits(['close-basket'])


const books = ref(props.basket.books)

const displayCloseButton = props.basket.dateClosed === ""


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

function closeBasket() {
  emit('close-basket', props.basket.id)
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