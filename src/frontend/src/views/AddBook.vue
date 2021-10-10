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
      </p>

      <label class="form-label" for="editor">Maison d'édition</label>
      <AutoComplete
          id="editor"
          v-model="editor"
          field="name"
          :dropdown="true"
          :suggestions="filteredEditors"
          @complete="searchEditors"
          @item-select="onEditorChange"
      />
      <p>
        <AuthorForm
            :add-author-function="addAuthor"
            :authors="authors"
            :delete-author-function="deleteAuthor"
        />
      </p>
      <h4>Auteur·rices</h4>

      <p>
        <label class="form-label" for="distributor">Distributeur</label>
        <AutoComplete
        id="distributor" v-model="distributor" :dropdown="true" :suggestions="filteredDistributors" @complete="searchDistributor"/>
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
import EditorService from "@/service/EditorService";
import DistributorService from "@/service/DistributorService";
import AuthorForm from "@/components/AuthorForm";

export default {
  name: "AddBook",
  components: {AuthorForm},
  el: '#addbook',
  data() {
    return {
      errors: [],
      isbn: null,
      title: null,
      authors: [({value: ''})],
      editor: null,
      editors: [],
      filteredEditors: [],
      distributor: null,
      distributors: [],
      filteredDistributors: [],
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
    deleteAuthor: function (index) {
      this.authors.splice(index, 1)
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
      EditorService.getAll()
        .then((response) => response.json())
        .then((data) => {
          // needded because Drodpown doesn't work with simple list
          this.editors = data
        })
      .catch(() => this.messages.push({severity: 'error', content: `Les éditeurs n'ont pas pu être chargés`}))
    },

    fetchDistributors: function () {
      DistributorService.getAll()
        .then((response) => response.json())
        .then((data) => {
          this.distributors = data
          console.log(data)
        })
        .catch(() => this.messages.push({severity: 'error', content: `Les distributeurs n'ont pas pu être chargés`}))
    },


    searchDistributor: function (event) {
      this.filteredDistributors = []
      this.distributors.forEach(e => {
        if (e.toLowerCase().includes(event.query.toLowerCase())) {
          this.filteredDistributors.push(e)
        }
      })
    },

    searchEditors: function (event) {
      console.log(event.value)
      this.filteredEditors = []
      this.editors.forEach(e => {
        if (e.name.toLowerCase().includes(event.query.toLowerCase())) {
          this.filteredEditors.push(e)
        }
      })
    },

    onEditorChange: function (event) {
      console.log(event)
      if (event.value && event.value.length !== 0) {
        console.log("inside first loop")
        console.log(this.editors)
        const editor = this.editors.find(e => e.name === event.value.name)
        if (editor && editor.defaultDistributor) {
          this.distributor = editor.defaultDistributor
        }
      }
    },
  },



  mounted() {
    this.fetchEditors();
    this.fetchDistributors()
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