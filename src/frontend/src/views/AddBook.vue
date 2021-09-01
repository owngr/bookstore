<template>
  <h1>Add book</h1>
  <div class="card">
    <form
        id="app"
        @submit.prevent="addBook"
    >

      <Message v-for="msg of messages" :sticky="false" :severity="msg.severity" :key="msg.content">{{msg.content}}</Message>

      <p>
        <label class="form-label" for="isbn">ISBN</label>
        <InputText
            @input="searchIsbn"
            class="form-control"
            id="isbn"
            v-model="isbn"
            type="number"
            name="isnbn"
            minlength="10"
            maxlength="13"
        />
      </p>

      <p>
        <label class="form-label" for="title">Titre</label>
        <InputText
            id="title"
            v-model="title"
            type="text"
            name="title"
        />
      </p>
      <p>
<!--        <label class="form-label" for="editor">Éditeur</label>-->
<!--        <InputText-->
<!--            id="editor"-->
<!--            type="text"-->
<!--            v-model="editor"-->
<!--            name="editor"-->
<!--        />-->
      </p>

      <h5>Editeur</h5>
      <Dropdown
          v-model="editor"
          :options="editors"
          :editable="true"
          :filter="true"
          :option-label="getEditorName"
          placeholder="Sélectionner un éditeur"
      />
      <h1>Auteur·rices</h1>
      <div v-for="(author, index) in authors" v-bind:key="index">
        <InputText v-bind:key="author.value" v-model="author.value"/>
      </div>
      <Button @click="addAuthor" label="nouve̛l·le auteur·rice"/>

      <p>
        <label class="form-label" for="distributor">Distributeur</label>
        <InputText
            id="distributor"
            class="form-control"
            v-model="distributor"
            type="text"
            name="distributor"
        />
      </p>
      <p>
        <Textarea v-model="description" rows="10" cols="100"></Textarea>
      </p>


      <p>
        <Button
            label="Ajouter le livre"
            type="submit"
            value="submit"
        />
      </p>

    </form>
  </div>
</template>

<script>

export default {
  name: "AddBook",
  el: '#addbook',
  data() {
    return {
      errors: [],
      isbn: null,
      title: null,
      authors: [({value: ''})],
      editor: null,
      editors: [],
      distributor: null,
      description: null,
      messages: [],
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
          description: this.description,
        })
      };
      fetch("/api/book/stock", requestOptions)
          .then(() => this.messages.push({severity: 'success', content: `Le livre a été ajouté`}))
          .catch(error => this.messages.push({severity: 'error', content: `Le livre n'a pas pu être ajouté: ${error}`}))
    },
    addAuthor: function () {
      this.authors.push({value: ''});
    },
    searchIsbn: function () {
      console.debug("fetching data")
      console.log(this.isbn.length)
      if (this.isbn.length === 13) {
        fetch("/api/book/ISBN?isbn=" + this.isbn)
            .then((response) => response.text())
            .then((data) => {
              this.fillData(JSON.parse(data));
            })
            .catch(() => this.messages.push({severity: 'warn', content: `Le livre n'a pas pu être trouvé`}))
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
      this.editor = data.editor
      this.description = data.description;
      console.log(this.authors);
    },

    // needed because primevue doesn't understand that it can take the value itself
    getEditorName: function (editor) {
      return editor
    },

    fetchEditors: function () {
      fetch("/api/editor")
        .then((response) => response.json())
        .then((data) => {
          // needded because Drodpown doesn't work with simple list
          this.editors = data
        })
      .catch(() => this.messages.push({severity: 'error', content: `Les éditeurs n'ont pas pu être chargés`}))
    }
  },

  mounted() {
    this.fetchEditors();
  },
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