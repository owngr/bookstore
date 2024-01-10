<template>
  <div class="card">
    <form id="app" @submit.prevent="processForm" @keydown.enter="processForm">
      <table class="w-full">
        <tr>
          <td class="w-2">
            <label class="form-label" for="isbn">{{
              i18n.global.t("isbn")
            }}</label>
          </td>
          <td class="w-6">
            <IsbnSearch
              :disabled="editMode"
              :init-isbn="book.isbn"
              class="w-full"
              @book="fillData"
              @prevent-submit="enableSubmit"
            />
          </td>
          <td rowspan="6">
            <img
              v-if="bookCopy.hasCover"
              :src="bookCopy.coverUrl"
              alt=""
              style="width: 200px; height: auto"
            />
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
              class="w-full"
              name="title"
              type="text"
            />
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="editor">{{
              i18n.global.t("editor")
            }}</label>
          </td>
          <td>
            <DropDown
              id="editor"
              v-model="bookCopy.editor"
              :filter="true"
              :options="editors"
              :placeholder="i18n.global.t('select')"
              class="w-full"
              editable
              option-label="name"
              @change="onEditorChange"
            />
          </td>
        </tr>

        <tr>
          <td style="vertical-align: top">
            <label class="form-label" for="authors">{{
              i18n.global.t("authors")
            }}</label>
          </td>
          <td>
            <AuthorForm
              :add-author-function="addAuthor"
              :authors="bookCopy.authors"
              :delete-author-function="deleteAuthor"
              class="w-full"
            />
          </td>
        </tr>

        <tr>
          <td>
            <label class="form-label" for="distributor">{{
              i18n.global.t("distributor")
            }}</label>
          </td>
          <td>
            <DropDown
              id="distributor"
              v-model="bookCopy.distributor"
              :editable="true"
              :filter="true"
              :options="distributors"
              :placeholder="i18n.global.t('select')"
            />
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <TextArea
              v-model="bookCopy.description"
              rows="10"
              style="width: 100%"
            ></TextArea>
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="price">{{
              i18n.global.t("price")
            }}</label>
          </td>
          <td>
            <InputNumber
              v-model="bookCopy.price"
              class="w-full"
              currency="CHF"
              mode="currency"
            />
          </td>
        </tr>

        <tr>
          <td>
            <label class="form-label" for="amount">{{
              i18n.global.t("quantity")
            }}</label>
          </td>
          <td>
            <InputNumber
              id="amount"
              v-model="bookCopy.amount"
              :show-buttons="false"
              class="w-full"
              name="amount"
            />
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="tags">{{
              i18n.global.t("tags")
            }}</label>
          </td>
          <td class="p-fluid">
            <AutoComplete
              id="tags"
              v-model="bookCopy.tags"
              :suggestions="tagsSuggestions"
              dropdown
              multiple
              name="tags"
              option-label="name"
              @complete="searchTags"
              @focusin="enableSubmit(true)"
              @focusout="enableSubmit(false)"
            />
          </td>
        </tr>
        <tr v-if="editMode">
          <td colspan="2">
            <FileUpload
              :custom-upload="true"
              accept="image/*"
              mode="advanced"
              name="demo[]"
              @uploader="fileUpload"
            />
          </td>
        </tr>
      </table>
      <p>
        <PButton :label="submitButtonText" type="submit" value="submit" />
      </p>
    </form>
  </div>
</template>
<script lang="ts" setup>
import AuthorForm from "@/components/AuthorForm";
import StockService from "@/service/StockService";
import IsbnSearch from "@/components/IsbnSearch";
import { defineEmits, defineProps, inject, ref, Ref } from "vue";
import { useFetchDistributors, useFetchEditors } from "@/composables/useFetch";
import i18n from "@/i18n";
import tagService from "@/service/TagService";
import { ScraperBook } from "@/models/ScraperBook";
import { Emitter } from "mitt";
import {MessageEvent} from "@/classes/Message";

const props = defineProps({
  book: ScraperBook,
  editMode: {
    type: Boolean,
    default: false,
  },
  submitButtonText: {
    type: String,
    default: i18n.global.t("addBook"),
  },
  processFormFunction: {},
});
let bookCopy = ref(JSON.parse(JSON.stringify(props.book)));
bookCopy.value.authors = bookCopy.value.authors.map((a) => {
  return { value: a };
});
let editors = useFetchEditors();
let distributors = useFetchDistributors();

const tags = ref<TagDto[]>([]);
const tagsSuggestions = ref<TagDto[]>([]);
const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>

loadTags();

function loadTags(): void {
  console.debug("loading tags");
  tagService
    .getTags()
    .then((data) => {
      tags.value = data;
    })
    .catch((e) =>
      emitter.emit(
        "notify",
          {severity: "error", content: i18n.global.t("couldNotLoadTagsMessage", { error: e })})
    );
}


function changeEditor(editorName: string): void {
  console.debug("editor name");
  console.log(editorName);
  const editor = editors.value.find((e) => e.name === editorName);
  if (editor && editor.defaultDistributor !== "null") {
    bookCopy.value.distributor = editor.defaultDistributor;
  }
  bookCopy.value.editor = editorName;
}

if (bookCopy.value.editor && bookCopy.value.editor.length > 0) {
  changeEditor(bookCopy.value.editor);
}

// let formData = ref(null);

let preventSubmit = ref<boolean>(false);

const emit = defineEmits(["close-dialog"]);

function fillData(data): void {
  console.debug("found data");
  console.debug(data);
  bookCopy.value.isbn = data.isbn;
  bookCopy.value.title = data.title;
  bookCopy.value.authors = data.authors.map((a) => {
    return { value: a };
  });
  bookCopy.value.editor = data.editor;
  bookCopy.value.distributor = data.distributor;
  bookCopy.value.description = data.description;
  bookCopy.value.price = data.price;
  bookCopy.value.coverUrl = data.coverUrl;
  bookCopy.value.hasCover = data.hasCover;
  bookCopy.value.tags = data.tags;
  console.debug("data filled");
}

function enableSubmit(enable: boolean): void {
  console.debug("in prevent submit" + enable);
  preventSubmit.value = enable;
}

function addAuthor(): void {
  bookCopy.value.authors.push({ value: "" });
}

function deleteAuthor(index: number): void {
  bookCopy.value.authors.splice(index, 1);
}

// needed because primevue doesn't understand that it can take the value itself
function processForm(): void {
  if (preventSubmit.value) {
    return;
  }
  if (!isFormValid()) {
    return;
  }
  props
    .processFormFunction(bookCopy.value)
    .then((data) => {
      bookCopy.value = data;
      // done in parent
      // this.messages.push({severity: 'success', content: `Le stock a été modifié`})
      emit("close-dialog");
    })
    .catch(() =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("stockNotUpdatableMessage"),
      })
    );
}

function isFormValid(): boolean {
  let isValid = true
  let error = ""
  if (!bookCopy.value.editor) {
    error = "missingPublisher"
    isValid = false
  } else if (!bookCopy.value.distributor) {
    error = "missingDistributor"
    isValid = false
  }
  if (!isValid) {
    emitter.emit("notify", {
      severity: "error",
      content: i18n.global.t(error),
    })
  }
  return isValid
}

function onEditorChange(event: Ref) {
  console.debug("type of event");
  console.debug(event);
  if (event.value && event.value.name !== undefined) {
    changeEditor(event.value.name);
  }
}

function fileUpload(event: Event): void {
  const formData = new FormData();
  formData.append("file", event.files[0]);
  StockService.addCover(formData, bookCopy.value.isbn)
    .then(() =>
      emitter.emit("notify", {
        severity: "success",
        content: i18n.global.t("imageUploadedMessage"),
      })
    )
    .catch(() =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("coulntUploadPictureMessage"),
      })
    );
}

function bookContainTag(tag: TagDto): boolean {
  for (const t of bookCopy.value.tags) {
    if (t.id === tag.id) {
      return true;
    }
  }
  return false;
}

function searchTags(event: Event): void {
  console.debug(event.query);
  tagsSuggestions.value = tags.value.filter((tag) => {
    return (
      tag.name.toLowerCase().startsWith(event.query.toLowerCase()) &&
      !bookContainTag(tag)
    );
  });
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