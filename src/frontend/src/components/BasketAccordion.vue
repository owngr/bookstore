<template>
  <div class="card flex flex-wrap gap-2 p-1">
    <PButton @click="exportCSV">{{ i18n.global.t("export") }}</PButton>
    <PButton
      v-if="displayCloseButton"
      class="p-button-danger"
      @click="closeBasket"
      >{{ i18n.global.t("closeBasket") }}
    </PButton>
  </div>

  <table>
    <tr>
      <th>{{ i18n.global.t("isbn") }}</th>
      <th>{{ i18n.global.t("title") }}</th>
      <th>{{ i18n.global.t("quantity") }}</th>
    </tr>
    <tr v-for="book in props.basket.books" :key="book.isbn">
      <td>{{ book.isbn }}</td>
      <td>{{ book.title }}</td>
      <td>{{ book.quantity }}</td>
    </tr>
  </table>
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, ref } from "vue";
import i18n from "@/i18n";
import { BasketDto } from "@/models/BasketDto";


const props = defineProps({
  basket: {
    type: BasketDto,
    default: new BasketDto(),
  },
});

const emit = defineEmits(["close-basket"]);

const books = ref(props.basket.books);

const displayCloseButton = props.basket.dateClosed === "";

function exportCSV(): void {
  let file = "ISBN,Titre,Quantité";
  books.value.forEach((b) => {
    file += `\n ${b.isbn},"${b.title}",${b.quantity}`;
  });
  const anchor = document.createElement("a");
  anchor.href = "data:text/csv;charset=utf-8," + encodeURIComponent(file);
  anchor.target = "_blank";
  anchor.download = props.basket.title + ".csv";
  anchor.click();
}

function closeBasket(): void {
  emit("close-basket", props.basket.id);
}
</script>

<style scoped>
th,
td {
  text-align: left;
  padding: 8px;
}

tr:nth-child(odd) {
  background-color: #f8f9fa;
}

td {
  border-top: 1px solid #ddd;
}

th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #f8f9fa;
}

table {
  width: 100%;
  border: 1px solid #ddd;
}
</style>