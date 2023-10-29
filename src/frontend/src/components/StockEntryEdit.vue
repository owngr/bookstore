<template>
  <div class="card">
    <form id="app" @submit.prevent="processForm" @keydown.enter="processForm">
      <table class="w-full">
        <tr>
          <td class="w-2">
            <label class="form-label" for="isbn">{{ $t("isbn") }}</label>
          </td>
          <td class="w-6">
            <IsbnSearch
              :disabled="editMode"
              :init-isbn="book.isbn"
              @book="fillData"
              @prevent-submit="enableSubmit"
              class="w-full"
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
              name="title"
              type="text"
              class="w-full"
            />
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="editor">{{ $t("editor") }}</label>
          </td>
          <td>
            <DropDown
              id="editor"
              v-model="bookCopy.editor"
              :editable="true"
              :filter="true"
              :options="editors"
              :placeholder="$t('select')"
              option-label="name"
              @change="onEditorChange"
              class="w-full"
            />
          </td>
        </tr>

        <tr>
          <td style="vertical-align: top">
            <label class="form-label" for="authors">{{ $t("authors") }}</label>
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
              $t("distributor")
            }}</label>
          </td>
          <td>
            <DropDown
              id="distributor"
              v-model="bookCopy.distributor"
              :editable="true"
              :filter="true"
              :options="distributors"
              :placeholder="$t('select')"
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
            <label class="form-label" for="price">{{ $t("price") }}</label>
          </td>
          <td>
            <InputNumber
              v-model="bookCopy.price"
              currency="CHF"
              mode="currency"
              class="w-full"
            />
          </td>
        </tr>

        <tr>
          <td>
            <label class="form-label" for="amount">{{ $t("quantity") }}</label>
          </td>
          <td>
            <InputNumber
              id="amount"
              v-model="bookCopy.amount"
              :show-buttons="false"
              name="amount"
              class="w-full"
            />
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="tags">{{ $t("tags") }}</label>
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
<script setup>
import AuthorForm from "@/components/AuthorForm";
import StockService from "@/service/StockService";
import IsbnSearch from "@/components/IsbnSearch";
import { defineEmits, defineProps, inject, ref } from "vue";
import { useFetchDistributors, useFetchEditors } from "@/composables/useFetch";
import i18n from "@/i18n";
import tagService from "@/service/TagService";

const props = defineProps({
  book: {},
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

const tags = ref([]);
const tagsSuggestions = ref([]);
loadTags();

function loadTags() {
  console.debug("loading tags");
  tagService
    .getTags()
    .then((data) => {
      tags.value = data;
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("couldNotLoadTagsMessage", { error: e }),
      })
    );
}

const emitter = inject("emitter");

const changeEditor = (editorName) => {
  console.log(editorName);
  const editor = editors.value.find((e) => e.name === editorName);
  if (editor && editor.defaultDistributor !== "null") {
    bookCopy.value.distributor = editor.defaultDistributor;
  }
  bookCopy.value.editor = editorName;
};
if (bookCopy.value.editor && bookCopy.value.editor.length > 0) {
  changeEditor(bookCopy.value.editor);
}

// let formData = ref(null);

let preventSubmit = ref(false);

const emit = defineEmits(["close-dialog"]);

const fillData = (data) => {
  console.debug("found data");
  console.debug(data);
  bookCopy.value.isbn = data.isbn;
  bookCopy.value.title = data.title;
  bookCopy.value.authors = data.authors.map((a) => {
    return { value: a };
  });
  bookCopy.value.editor = data.editor;
  bookCopy.value.description = data.description;
  if (bookCopy.value.editor && bookCopy.value.editor.length > 0) {
    changeEditor(bookCopy.value.editor);
  }
  bookCopy.value.price = data.price;
  bookCopy.value.coverUrl = data.coverUrl;
  bookCopy.value.hasCover = data.hasCover;
  bookCopy.value.tags = data.tags;
  console.debug("data filled");
};

const enableSubmit = (enable) => {
  console.debug("in prevent submit" + enable)
  preventSubmit.value = enable;
};

const addAuthor = () => {
  bookCopy.value.authors.push({ value: "" });
};

const deleteAuthor = (index) => {
  bookCopy.value.authors.splice(index, 1);
};

// needed because primevue doesn't understand that it can take the value itself
const processForm = () => {
  if (preventSubmit.value) {
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
};

const onEditorChange = (event) => {
  if (event.value && event.value.name !== null) {
    changeEditor(event.value.name);
  }
};

const fileUpload = (event) => {
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
};

const bookContainTag = (tag) => {
  for (const t of bookCopy.value.tags) {
    if (t.id === tag.id) {
      return true
    }
  }
  return false
}
const searchTags = (event) => {
  console.debug(event.query);
  tagsSuggestions.value = tags.value.filter((tag) => {
    return tag.name.toLowerCase().startsWith(event.query.toLowerCase()) && !bookContainTag(tag);
  });
};
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
