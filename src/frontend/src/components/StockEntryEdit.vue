<template>
  <div class="card">
    <form
        id="app"
        @submit.prevent="updateStockEntry"
    >

      <Message v-for="msg of messages" :sticky="false" :severity="msg.severity" :key="msg.content">{{msg.content}}</Message>

      <p>
        <label class="form-label" for="isbn">ISBN</label>
        <InputText
            @input="searchIsbn"
            class="form-control"
            id="isbn"
            v-model="bookCopy.isbn"
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
            v-model="bookCopy.title"
            type="text"
            name="title"
        />
      </p>
      <p>
      </p>

      <h5>Editeur</h5>
      <Dropdown
          v-model="bookCopy.editor"
          :options="editors"
          :editable="true"
          :filter="true"
          :option-label="getEditorName"
          placeholder="Sélectionner un éditeur"
      />
      <h1>Auteur·rices</h1>
      <div v-for="(author, index) in bookCopy.authors" v-bind:key="index">
        <InputText v-bind:key="author.value" v-model="author.value"/>
      </div>
      <Button @click="addAuthor" label="nouve̛l·le auteur·rice"/>

      <p>
        <label class="form-label" for="distributor">Distributeur</label>
        <InputText
            id="distributor"
            class="form-control"
            v-model="bookCopy.distributor"
            type="text"
            name="distributor"
        />
      </p>
      <p>
        <Textarea v-model="bookCopy.description" rows="10" cols="100"></Textarea>
      </p>

      <p>
        <label class="form-label" for="amount">Quantité</label>
        <InputText
            class="form-control"
            id="amount"
            v-model="bookCopy.amount"
            type="number"
            name="amount"
        />
      </p>

      <p>
        <Button
            label="Mettre à jour le livre"
            type="submit"
            value="submit"
        />
      </p>



    </form>
  </div>
</template>

<script>
import EditorService from "@/service/EditorService";
import StockService from "@/service/StockService";

export default {
  name: "StockEntryEdit",
  props: ["book"],
  data() {
    return {
      bookCopy: {},
      editors: [],
      messages: [],
      authors: []
    }
  },
  methods: {
    addAuthor: function () {
      this.bookCopy.authors.push({value: ''});
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
    updateStockEntry: function () {

      StockService.updateStock(this.bookCopy)
        .then((data) => {
          this.bookCopy = data
          this.messages.push({severity: 'success', content: `Le stock a été modifié`})
          this.$emit('close-dialog')
        })
        .catch(() => this.messages.push({severity: 'error', content: `le stock n'a pas pu être modifié`}))
    }
  },
  created() {
    // this.bookCopy =
    this.bookCopy = JSON.parse(JSON.stringify(this.book))
    this.bookCopy.authors = this.bookCopy.authors.map(a => {
      return {value: a}
    });
    console.log(this.bookCopy.authors)
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