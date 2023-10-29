<template>
  <h1>{{ $t('addBook') }}</h1>
  <StockEntryEdit
      :key="reloadCount"
      v-model:book="initBook"
      :process-form-function="addBook"
      class="w-64rem"
  />

</template>

<script>
import StockEntryEdit from "@/components/StockEntryEdit";
import StockService from "@/service/StockService";
import i18n from "@/i18n";
import {inject} from "vue";

export default {
  el: '#addbook',
  name: "AddBook",
  components: {StockEntryEdit},
  data() {
    return {
      reloadCount: 0,
      initBook: {
        isbn: null,
        title: null,
        authors: [],
        editor: null,
        distributor: null,
        description: null,
        price: null,
        coverUrl: null,
        amount: 1,
      },
      emitter: inject('emitter')
    }
  },
  methods: {
    addBook: function (book) {
      console.debug("addBook.addbook")
      const body = {
        isbn: book.isbn,
        title: book.title,
        // authors: null,
        authors: book.authors.map(a => a.value),
        editor: book.editor,
        distributor: book.distributor,
        description: book.description,
        price: book.price,
        coverUrl: book.coverUrl,
        hasCover: book.hasCover,
        amount: book.amount,
        tags: book.tags,
      }
      this.reloadCount++
      this.emitter.emit('notify',{severity: 'success', content: i18n.global.t('stockHasBeenModifiedMessage')})
      return StockService.addBook(body)
    },
  }

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