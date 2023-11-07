<template>
  <div class="card">
    <form id="app" @submit.prevent="processForm">
      <table>
        <tr v-if="editMode">
          <td>
            <label class="form-label" for="id">{{ i18n.global.t("id") }}</label>
          </td>
          <td>{{tagCopy.id}}</td>
        </tr>
        <tr>
          <td>
            <label class="form-label" for="name">{{ i18n.global.t("name") }}</label>
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
            <label class="form-label" for="primary">{{ i18n.global.t("primary") }}</label>
          </td>
          <td>
            <InputSwitch v-model="tagCopy.main" input-id="primary" />
          </td>
        </tr>
      </table>
      <div class="card flex flex-wrap gap-2 p-1">
        <PButton :label="submitButtonText" type="submit" value="submit" />
        <PButton v-if="editMode" :label="i18n.global.t('delete')" class="p-button-danger" @click="deleteTag"/>
      </div>

    </form>
  </div>
</template>

<script setup lang="ts">
import { defineEmits, defineProps, inject, ref } from "vue";
import i18n from "@/i18n";
import tagService from "@/service/TagService";
import TagDto from "@/models/TagDto";
import {Emitter} from "mitt";
import {MessageEvent} from "@/classes/Message";


const props = defineProps({
  tag: TagDto,
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

let tagCopy = ref<TagDto>(JSON.parse(JSON.stringify(props.tag)));

const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>
let preventSubmit = ref(false);
const emit = defineEmits(["close-dialog"]);

function processForm() : void{
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

function deleteTag() : void {
  tagService.deleteTag(tagCopy.value.id)
      .then(() => {
        emitter.emit('notify', {severity: 'success', content: i18n.global.t('tagDeletedMessage')})
        emit("close-dialog")
      })
      .catch(() => emitter.emit('notify', {severity: 'error', content: i18n.global.t('couldntDeleteTagMessage')}))
}
</script>

<style scoped></style>
