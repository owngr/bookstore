<template>
  <div class="card">
    <form
        id="app"
        @submit.prevent="processForm"
    >

      <Message v-for="msg of messages" :sticky="false" :severity="msg.severity" :key="msg.content">{{msg.content}}</Message>


      <table>
        <tr>
          <td>
            <label class="form-label" for="isbn">ISBN</label>
          </td>
          <td>
            <IsbnSearch
                @book="fillData"
                @message="this.messages.push($event)"
                :disabled="editMode"
            />
          </td>
          <td rowspan="6">
            <img v-if="!editMode" :src="bookCopy.coverUrl" alt="" style="width: 200px; height: auto">
            <img v-if="editMode && bookCopy.hasCover" :src="'/api/shop/'+bookCopy.isbn" alt="" style="width: 200px; height: auto">
          </td>

        </tr>

        <tr>
          <td>
            <label class="form-label" for="title">Titre</label>
          </td>
          <td>
            <InputText
                id="title"
                v-model="bookCopy.title"
                type="text"
                name="title"
            />
          </td>
        </tr>
        <tr>
          <td><label class="form-label" for="editor">Maison d'édition</label></td>
          <td>
            <AutoComplete
                id="editor"
                v-model="bookCopy.editor"
                field="name"
                :dropdown="true"
                :suggestions="filteredEditors"
                @complete="searchEditors"
                @item-select="onEditorChange"
            />
          </td>
        </tr>


        <tr>
          <td style="vertical-align: top">
            <label class="form-label" for="authors">Auteur·ices</label>
          </td>
          <td>
            <AuthorForm
                :add-author-function="addAuthor"
                :authors="bookCopy.authors"
                :delete-author-function="deleteAuthor"
            />
          </td>
        </tr>

        <tr>
          <td>
            <label class="form-label" for="distributor">Distributeur</label>
          </td>
          <td>
            <AutoComplete
                id="distributor" v-model="bookCopy.distributor"
                :dropdown="true" :suggestions="filteredDistributors"
                optionValue="name"
                @complete="searchDistributor"/>
          </td>

        </tr>
        <tr>
          <td colspan="2">
            <Textarea v-model="bookCopy.description" rows="10" style="width: 100%"></Textarea>
          </td>

        </tr>
        <tr>
          <td>
            <label class="form-label" for="price">Prix</label>
          </td>
          <td>
            <InputNumber v-model="bookCopy.price" mode="currency" currency="CHF"/>
          </td>
        </tr>

        <tr v-if="editMode">
          <td>
            <label class="form-label" for="amount">Quantité</label>
          </td>
          <td>
            <InputText
                class="form-control"
                id="amount"
                v-model="bookCopy.amount"
                type="number"
                name="amount"
            />
          </td>

        </tr>
        <tr v-if="editMode">
          <td colspan="2">
            <FileUpload mode="advanced" name="demo[]" @uploader="fileUpload" :customUpload="true" accept="image/*" />
          </td>
        </tr>
      </table>
      <p>
        <Button
            :label="submitButtonText"
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
import StockService from "@/service/StockService";
import IsbnSearch from "@/components/IsbnSearch";
export default {
  name: "StockEntryEdit",
  components: {IsbnSearch, AuthorForm },
  props: {
    book: {},
    editMode: {
      type: Boolean,
      default: false,
    },
    // used after reload
    initMessages: [],
    submitButtonText: {
      type: Text,
      default: "Ajouter le livre",
    },
    processFormFunction: {}
  },
  data() {
    return {
      bookCopy: {},
      editors: [],
      messages: [],
      authors: [],
      filteredEditors: [],
      distributors: [],
      filteredDistributors: [],
      formData: null
    }
  },
  methods: {
    fillData: function (data) {
      console.debug('found data')
      this.bookCopy.isbn = data.isbn;
      this.bookCopy.title = data.title;
      this.bookCopy.authors = data.authors.map(a => {
        return {value: a}
      });
      this.bookCopy.editor = data.editor
      this.bookCopy.description = data.description;
      if (this.bookCopy.editor && this.bookCopy.editor.length > 0) {
        this.changeEditor(this.bookCopy.editor)
      }
      this.bookCopy.price = data.price
      this.bookCopy.coverUrl = data.coverUrl
      console.debug("data filled")
    },
    addAuthor: function () {
      this.bookCopy.authors.push({value: ''});
    },
    deleteAuthor: function (index) {
      this.bookCopy.authors.splice(index, 1)
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
    // needed because primevue doesn't understand that it can take the value itself
    getEditorName: function (editor) {
      return editor
    },
    processForm: function () {
      this.processFormFunction(this.bookCopy)
        .then((data) => {
          this.bookCopy = data
          // done in parent
          // this.messages.push({severity: 'success', content: `Le stock a été modifié`})
          this.$emit('close-dialog')
        })
        .catch(() => this.messages.push({severity: 'error', content: `le stock n'a pas pu être modifié`}))
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
    changeEditor: function (editorName) {
      console.log(editorName)
      const editor = this.editors.find(e => e.name === editorName)
      if (editor && editor.defaultDistributor) {
        this.bookCopy.distributor = editor.defaultDistributor
      }
    },

    onEditorChange: function (event) {
      if (event.value && event.value.length !== 0) {
        const editorName = event.value.name
        this.bookCopy.editor = event.value.name
        this.changeEditor(editorName);
      }
    },

    fileUpload(event) {
      this.formData = new FormData();
      this.formData.append("file", event.files[0])
      StockService.addCover(this.formData, this.bookCopy.isbn)
          .then(() => this.messages.push({severity: 'success', content: `L'image a pu être uploadé`}))
          .catch(() => this.messages.push({severity: 'error', content: `L'image n'a pas pu être uploadé`}))
    },
  },


  created() {
    // this.bookCopy =
    this.bookCopy = JSON.parse(JSON.stringify(this.book))
    this.messages = this.initMessages
    this.bookCopy.authors = this.bookCopy.authors.map(a => {
      return {value: a}
    });
    if (this.bookCopy.editor && this.bookCopy.editor.length > 0) {
      this.changeEditor(this.bookCopy.editor)
    }
    console.log(this.bookCopy.authors)
  },
  mounted() {
    this.fetchEditors();
    this.fetchDistributors();
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