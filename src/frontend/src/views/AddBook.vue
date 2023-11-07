<template>
  <h1>{{ i18n.global.t("addBook") }}</h1>
  <StockEntryEdit
    :key="reloadCount"
    v-model:book="initBook"
    :process-form-function="addBook"
    class="w-64rem"
  />
</template>

<script lang="ts" setup>
import StockEntryEdit from "@/components/StockEntryEdit";
import StockService from "@/service/StockService";
import i18n from "@/i18n";
import { inject, ref } from "vue";
import { ScraperBook } from "@/models/ScraperBook";
import { Emitter } from "mitt"
import {MessageEvent} from "@/classes/Message";

const reloadCount = ref(0);
const initBook = ref(new ScraperBook());
initBook.value.amount = 1;

const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>

function addBook(book: ScraperBook) {
  const body = {
    isbn: book.isbn,
    title: book.title,
    // authors: null,
    authors: book.authors.map((a) => a.value),
    editor: book.editor,
    distributor: book.distributor,
    description: book.description,
    price: book.price,
    coverUrl: book.coverUrl,
    hasCover: book.hasCover,
    amount: book.amount,
    tags: book.tags,
  };
  reloadCount.value++;
  emitter.emit("notify", {
    severity: "success",
    content: i18n.global.t("stockHasBeenModifiedMessage")
  });
  return StockService.addBook(body);
}
</script>

<style scoped>
div :deep(.p-hidden-accessible) {
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