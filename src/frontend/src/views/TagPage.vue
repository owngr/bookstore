<template>
  <h1>{{ i18n.global.t("tags") }}</h1>
  <PButton @click="openAddDialog()"
  >{{ i18n.global.t("addTag") }}
  </PButton>
  <PDialog
      v-model:visible="displayAddDialog"
      :header="i18n.global.t('addTag')"
      :style="{ width: '50vw' }">
    <TagEdit
        v-model:tag="initTag"
        :edit-mode="false"
        :process-form-function="addTag"
        :submit-button-text="i18n.global.t('addTag')"
        @close-dialog="closeEditDialog"/>
  </PDialog>
  <PDialog
    v-model:visible="displayEditDialog"
    :header="i18n.global.t('updateTag')"
    :style="{ width: '50vw' }">
    <TagEdit
      v-model:tag="tag"
      :edit-mode="true"
      :process-form-function="updateTag"
      :submit-button-text="i18n.global.t('updateTag')"
      @close-dialog="closeEditDialog"/>
  </PDialog>
  <DataTable ref="dt" :value="tags" responsive-layout="scroll">
    <PColumn field="name" :header="i18n.global.t('name')" />
    <PColumn field="main" :header="i18n.global.t('primaryTag')">
      <template #body="slotProps">
        {{ i18n.global.t(slotProps.data.main.toString()) }}
      </template>
    </PColumn>
    <PColumn>
      <template #body="slotProps">
        <PButton @click="openEditDialog(slotProps.data)"
          >{{ i18n.global.t("edit") }}
        </PButton>
      </template>
    </PColumn>
  </DataTable>
</template>
<script setup lang="ts">
import { inject, ref } from "vue";
import tagService from "@/service/TagService";
import i18n from "@/i18n";
import TagEdit from "@/components/TagEdit.vue";
import {Emitter} from "mitt";
import {MessageEvent} from "@/classes/Message";

const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>;

const tags = ref<TagDto[]>([]);
let displayEditDialog = ref(false)
let displayAddDialog = ref(false)
let tag = ref<TagDto>({})
const initTag = ref<TagDto>({id: -1, name: "", main: false})
loadTags();

function loadTags() {
  console.debug("loading tags")
  tagService
    .getTags()
    .then((data) => {
      tags.value = data;
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("couldNotLoadTagsMessage", {error: e}),
      })
    );
}

function openEditDialog(rowTag: TagDto) {
  tag.value = rowTag;
  displayEditDialog.value = true
  console.log(tag)
}

function updateTag(tag: TagDto) {
  return tagService.updateTag(tag)
      .then(() => {
        emitter.emit("notify", {
          severity: "success",
          content: i18n.global.t('tagUpdatedMessage')
        })
        closeEditDialog()
      })
}

function addTag(tag: TagDto) {
  closeAddDialog()
  return tagService.addTag(tag)
      .then(loadTags)
}

function closeEditDialog() {
  displayEditDialog.value = false
  loadTags()
}

function openAddDialog() {
  displayAddDialog.value = true
}

function closeAddDialog() {
  displayAddDialog.value = false
}
</script>

<style scoped></style>
