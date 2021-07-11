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
      <h1>Auteurs</h1>
      <div v-for="(author, index) in authors" v-bind:key="index">
        <input v-bind:key="author.value">
      </div>
      <button @click="addAuthor">
        New Author
      </button>
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
        <input
            type="submit"
            value="Submit"
            class="btn btn-primary"
        >
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
      authors: [{value: ''}],
      editor: null,
      distributor: null,
    }
  },
  methods: {
    addAuthor: function () {
      this.authors.push(({value: ''}));
    }
  },
  //   checkForm: function (e) {
  //     if (this.name && this.age) {
  //       return true;
  //     }
  //
  //     this.errors = [];
  //
  //     if (!this.name) {
  //       this.errors.push('Name required.');
  //     }
  //     if (!this.age) {
  //       this.errors.push('Age required.');
  //     }
  //
  //     e.preventDefault();
  //   }
  // },
  setup() {
    // const errors = [];
    // const name = ref(null);
    // const age = ref(null);
    // const movie = ref(null);
    // function checkForm(e) {
    //   if (this.name && this.age) {
    //     return true;
    //   }
    //
    //   this.errors = [];
    //
    //   if (!this.name) {
    //     this.errors.push('Name required.');
    //   }
    //   if (!this.age) {
    //     this.errors.push('Age required.');
    //   }
    //
    //   e.preventDefault();
    // }
  }
}
</script>

<style scoped>

</style>