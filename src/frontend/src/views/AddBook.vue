<template>
  <h1>Add book</h1>
  <div class="card">
    <form
        id="app"
        @submit="checkForm"
        action="https://vuejs.org/"
        method="post"
    >

      <p v-if="errors.length">
        <b>Please correct the following error(s):</b>
      <ul>
        <li v-bind="error in errors">{{ error }}</li>
      </ul>
      </p>

      <p>
        <label class="form-label" for="isbn">ISBN</label>
        <input
            @input="searchIsbn"
            class="form-control"
            id="isbn"
            v-model="isbn"
            type="number"
            name="isnbn"
            minlength="10"
            maxlength="13"
        >
      </p>

      <p>
        <label class="form-label" for="title">Titre</label>
        <input
            class="form-control"
            id="title"
            v-model="title"
            type="text"
            name="title"
        >
      </p>
      <p>
        <label class="form-label" for="editor">Éditeur</label>
        <input
            id="editor"
            type="text"
            class="form-control"
            v-model="editor"
            name="editor"
        >
      </p>
      <h1>Auteurs</h1>
      <div v-for="(author, index) in authors" v-bind:key="index">
        <input v-bind:key="author.value" v-model="author.value">
      </div>
      <button @click="addAuthor" class="btn btn-secondary">
        New Author
      </button>

      <p>
        <label class="form-label" for="distributor">Distributeur</label>
        <input
            id="distributor"
            class="form-control"
            v-model="distributor"
            type="text"
            name="distributor"
        >
      </p>


      <p>
        <button
            class="btn btn-primary"
            @click="addBook"
            type="button"
            value="submit"
        >
          Add book
        </button>
      </p>

    </form>
  </div>
</template>

<script>

export default {
  name: "AddBook",
  el: '#app',
  data() {
    return {
      errors: [],
      isbn: null,
      title: null,
      aauthors: [({value: ''})],
      editor: null,
      distributor: null,
    }
  },
  methods: {
    addBook: function () {
      const requestOptions = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
          isbn: this.isbn,
          title: this.title,
          // authors: null,
          authors: this.authors.map(a => a.value),
          editor: this.editor,
          distributor: this.distributor,
        })
      };
      fetch("/api/book/stock", requestOptions)
          .then(response => response.json())
          .then(data => console.log(data));
    },
    addAuthor: function () {
      this.authors.push({value: ''});
    },
    searchIsbn: function () {
      console.debug("fetching data")
      console.log(this.isbn.length)
      if (this.isbn.length === 10 || this.isbn.length === 13) {
        fetch("/api/book/ISBN?isbn=" + this.isbn)
            .then((response) => response.text())
            .then((data) => {
              this.fillData(JSON.parse(data));
            });
      }
    },
    fillData: function (data) {
      console.log(data);
      this.isbn = data.isbn;
      this.title = data.title;
      console.log(typeof data.authors);
      console.log(this.authors);
      this.authors = data.authors.map(a => {
        return {value: a}
      });
      console.log(this.authors);
    }
  },

  setup() {

  }
}
</script>

<style scoped>

</style>