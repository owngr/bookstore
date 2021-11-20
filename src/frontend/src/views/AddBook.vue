<template>
  <h1>Add book</h1>
  <StockEntryEdit
      v-model:book="initBook"
      :process-form-function="addBook"
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
      initBook: {
        isbn: null,
        title: null,
        authors: [],
        editor: null,
        distributor: null,
        description: null,
        price: null,
      }
    }
  },
  methods: {
    addBook: function (book) {
      const body = {
        isbn: book.isbn,
        title: book.title,
        // authors: null,
        authors: book.authors.map(a => a.value),
        editor: book.editor,
        distributor: book.distributor,
        description: book.description,
        price: book.price
      }
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