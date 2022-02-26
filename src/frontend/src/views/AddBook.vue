<template>
  <h1>Add book</h1>
  <StockEntryEdit
      v-model:book="initBook"
      :process-form-function="addBook"
      v-model:initMessages="messages"
      :key="reloadCount"
  />

</template>

<script>
import StockEntryEdit from "@/components/StockEntryEdit";
import StockService from "@/service/StockService";

export default {
  name: "AddBook",
  components: {StockEntryEdit},
  el: '#addbook',
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
      },
      messages: []
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
      }
      this.reloadCount++
      this.messages.push({severity: 'success', content: `Le stock a été modifié`})
      return StockService.addBook(body)
    },
  }

}
</script>

<style scoped>

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