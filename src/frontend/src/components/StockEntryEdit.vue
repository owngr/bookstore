<template>
  <div class="card">
    <form
        id="app"
        @submit.prevent="processForm"
        @keydown.enter="processForm"
    >

      <PMessage v-for="msg of messages" :key="msg.content" :severity="msg.severity" :sticky="false">{{ msg.content }}
      </PMessage>


      <table>
        <tr>
          <td>
            <label class="form-label" for="isbn">{{ $t('isbn') }}</label>
          </td>
          <td>
            <IsbnSearch
                :disabled="editMode"
                :init-isbn="book.isbn"
                @book="fillData"
                @message="messages.push($event)"
                @prevent-submit="enableSubmit"
            />
          </td>
          <td rowspan="6">
            <img v-if="!editMode" :src="bookCopy.coverUrl" alt="" style="width: 200px; height: auto">
            <img
                v-if="editMode && bookCopy.hasCover" :src="'/api/shop/'+bookCopy.isbn" alt=""
                style="width: 200px; height: auto">
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
          <td><label class="form-label" for="editor">{{ $t('editor') }}</label></td>
          <td>
            <DropDown
                id="editor"
                v-model="bookCopy.editor"
                :editable="true"
                :options="editors"
                :filter="true"
                option-label="name"
                :placeholder="$t('select')"
                @change="onEditorChange"
            />
          </td>
        </tr>


        <tr>
          <td style="vertical-align: top">
            <label class="form-label" for="authors">{{ $t('authors') }}</label>
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
            <label class="form-label" for="distributor">{{ $t('distributor') }}</label>
          </td>
          <td>
            <DropDown
                id="distributor" v-model="bookCopy.distributor"
                :filter="true" :options="distributors"
                :editable="true"
                :placeholder="$t('select')"/>
          </td>

        </tr>
        <tr>
          <td colspan="2">
            <TextArea v-model="bookCopy.description" rows="10" style="width: 100%"></TextArea>
          </td>

        </tr>
        <tr>
          <td>
            <label class="form-label" for="price">{{ $t('price') }}</label>
          </td>
          <td>
            <InputNumber v-model="bookCopy.price" mode="currency" currency="CHF"/>
          </td>
        </tr>

        <tr>
          <td>
            <label class="form-label" for="amount">{{ $t('quantity') }}</label>
          </td>
          <td>
            <InputNumber
                id="amount"
                v-model="bookCopy.amount"
                name="amount"
                :show-buttons="false"
            />
          </td>

        </tr>
        <tr v-if="editMode">
          <td colspan="2">
            <FileUpload mode="advanced" name="demo[]" :custom-upload="true" accept="image/*" @uploader="fileUpload"/>
          </td>
        </tr>
      </table>
      <p>
        <PButton
            :label="submitButtonText"
            type="submit"
            value="submit"
        />
      </p>


    </form>
  </div>
</template>
<script setup>

import AuthorForm from "@/components/AuthorForm"
import StockService from "@/service/StockService"
import IsbnSearch from "@/components/IsbnSearch"
import {defineEmits, defineProps, ref,} from "vue"
import {useFetchDistributors, useFetchEditors} from "@/composables/useFetch"
import i18n from "@/i18n";


const props = defineProps({
  book: {},
  editMode: {
    type: Boolean,
    default: false,
  },
  // used after reload
  initMessages: [],
  submitButtonText: {
    type: String,
    default: i18n.global.t('addBook'),
  },
  processFormFunction: {}
})
let bookCopy = ref(JSON.parse(JSON.stringify(props.book)))
bookCopy.value.authors = bookCopy.value.authors.map(a => {
  return {value: a}
})
let messages = ref(props.initMessages)
let editors
({editors, messages} = useFetchEditors())
let distributors
({distributors, messages} = useFetchDistributors())


const changeEditor = (editorName) => {
  console.log(editorName)
  const editor = editors.value.find(e => e.name === editorName)
  if (editor && editor.defaultDistributor !== "null") {
    bookCopy.value.distributor = editor.defaultDistributor
  }
  bookCopy.value.editor = editorName
}
if (bookCopy.value.editor && bookCopy.value.editor.length > 0) {
  changeEditor(bookCopy.value.editor)
}

let formData = ref(null)

let preventSubmit = ref(false)

const emit = defineEmits(['close-dialog'])

const fillData = (data) => {
  console.debug('found data')
  console.debug(data)
  bookCopy.value.isbn = data.isbn
  bookCopy.value.title = data.title
  bookCopy.value.authors = data.authors.map(a => {
    return {value: a}
  })
  bookCopy.value.editor = data.editor
  bookCopy.value.description = data.description
  if (bookCopy.value.editor && bookCopy.value.editor.length > 0) {
    changeEditor(bookCopy.value.editor)
  }
  bookCopy.value.price = data.price
  bookCopy.value.coverUrl = data.coverUrl
  console.debug("data filled")
}

const enableSubmit = (enable) => {
  preventSubmit.value = enable
}

const addAuthor = () => {
  bookCopy.value.authors.push({value: ''})
}

const deleteAuthor = (index) => {
  bookCopy.value.authors.splice(index, 1)
}

// needed because primevue doesn't understand that it can take the value itself
const processForm = () => {
  if (preventSubmit.value) {
    return
  }
  props.processFormFunction(bookCopy.value)
      .then((data) => {
        bookCopy.value = data
        // done in parent
        // this.messages.push({severity: 'success', content: `Le stock a été modifié`})
        emit('close-dialog')
      })
      .catch(() => messages.value.push({severity: 'error', content: i18n.global.t('stockNotUpdatableMessage')}))
}


const onEditorChange = (event) => {
  if (event.value && event.value.name !== null) {
    changeEditor(event.value.name)
  }
}

const fileUpload = (event) => {
  formData.value = new FormData()
  formData.value.append("file", event.files[0])
  StockService.addCover(formData, bookCopy.value.isbn)
      .then(() => messages.value.push({severity: 'success', content: i18n.global.t('imageUploadedMessage')}))
      .catch(() => messages.value.push({severity: 'error', content: i18n.global.t('coulntUploadPictureMessage')}))
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