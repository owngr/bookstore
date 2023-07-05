<template>
  <div class="card">
    <form id="app" @submit.prevent="processForm">
      <table>
        <tr v-if="editMode">
          <td>
            <label class="form-label" for="id">{{ $t("id") }}</label>
          </td>
          <td>{{tagCopy.id}}</td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="name">{{ $t("name") }}</label>
          </td>
          <td>
            <InputText
              id="name"
              v-model="tagCopy.name"
              name="name"
              type="text"
            />
          </td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="primary">{{ $t("primary") }}</label>
          </td>
          <td>
            <InputSwitch v-model="tagCopy.main" input-id="primary" />
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
import { defineEmits, defineProps, inject, ref } from "vue";
import i18n from "@/i18n";

const props = defineProps({
  tag: {},
  editMode: {
    type: Boolean,
    default: false,
  },
  submitButtonText: {
    type: String,
    default: i18n.global.t("addTag"),
  },
  processFormFunction: {},
});

let tagCopy = ref(JSON.parse(JSON.stringify(props.tag)));

const emitter = inject("emitter");
let preventSubmit = ref(false);
const emit = defineEmits(["close-dialog"]);

function processForm() {
  console.debug("in processform")
  console.debug(preventSubmit)
  if (preventSubmit.value) {
    return;
  }
  props
    .processFormFunction(tagCopy.value)
    .then((data) => {
      tagCopy.value = data;
      emit("close-dialog");
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("tagNotUpdatableMessage", { error: e }),
      })
    );
}
</script>

<style scoped></style>
