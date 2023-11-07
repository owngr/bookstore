<template>
  <InputText
    id="isbn"
    ref="isbnsearchRef"
    v-model="isbn"
    :disabled="disabled"
    class="form-control"
    maxlength="13"
    minlength="10"
    name="isnbn"
    type="number"
    @focusin="emitPreventSubmit(true)"
    @focusout="emitPreventSubmit(false)"
    @keyup.enter="searchIsbn"
  />
</template>

<script lang="ts" setup>
import { defineEmits, defineProps, inject, onMounted, ref } from "vue";
import i18n from "@/i18n";

const emit = defineEmits(["book", "preventSubmit"]);
const emitter = inject("emitter");

const props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
  apiPath: {
    type: String,
    default: "/api/book/ISBN?isbn=",
  },
  resetOnFound: {
    type: Boolean,
    default: false,
  },
  initIsbn: {
    type: String,
    default: "",
  },
});
console.log(props.initIsbn);
const isbnsearchRef = ref(null);
const isbn = ref(props.initIsbn);
const preventSubmit = ref(false);

// we set the focus on isbn if no isbn is present
onMounted(() => {
  if (!isbn.value) {
    isbnsearchRef.value.$el.focus();
  }
});

function searchIsbn(): void {
  console.debug("fetching data");
  console.log(isbn.value);
  if (!preventSubmit.value) {
    return;
  }
  if (isbn.value && isbn.value.length === 13) {
    fetch(props.apiPath + isbn.value)
      .then((response) => response.text())
      .then((data) => {
        if (props.resetOnFound) {
          isbn.value = "";
        }
        emit("book", JSON.parse(data));
      })
      .catch((e) =>
        emitter.emit("notify", {
          severity: "warn",
          content: i18n.global.t("bookNotFoundMessage", { error: e }),
        })
      );
  }
}

function emitPreventSubmit(prevent: ref<boolean>) {
  preventSubmit.value = prevent;
  emit("preventSubmit", prevent);
}
</script>

<style scoped></style>