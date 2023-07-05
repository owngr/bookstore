<template>
  <h1>{{ $t("tags") }}</h1>
  <PButton @click="openAddDialog()"
  >{{ $t("addTag") }}
  </PButton>
  <PDialog
      v-model:visible="displayAddDialog"
      :header="$t('addTag')"
      :style="{ width: '50vw' }">
    <TagEdit
        v-model:tag="initTag"
        :edit-mode="false"
        :process-form-function="addTag"
        :submit-button-text="$t('addTag')"
        @close-dialog="closeEditDialog"/>
  </PDialog>
  <PDialog
    v-model:visible="displayEditDialog"
    :header="$t('updateTag')"
    :style="{ width: '50vw' }">
    <TagEdit
      v-model:tag="tag"
      :edit-mode="true"
      :process-form-function="updateTag"
      :submit-button-text="$t('updateTag')"
      @close-dialog="closeEditDialog"/>
  </PDialog>
  <DataTable ref="dt" :value="tags" responsive-layout="scroll">
    <PColumn field="name" header="name" />
    <PColumn field="main" header="primaryTag" />
    <PColumn>
      <template #body="slotProps">
        <PButton @click="openEditDialog(slotProps.data)"
          >{{ $t("edit") }}
        </PButton>
      </template>
    </PColumn>
  </DataTable>
</template>
<script setup>
import { inject, ref } from "vue";
import tagService from "@/service/TagService";
import i18n from "@/i18n";
import TagEdit from "@/components/TagEdit.vue";

const emitter = inject("emitter");

const tags = ref([]);
let displayEditDialog = ref(false)
let displayAddDialog = ref(false)
let tag = ref({})
const initTag = ref({id: -1, name: "", main: false})
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

function openEditDialog(rowTag) {
  tag.value = rowTag;
  displayEditDialog.value = true
  console.log(tag)
}

function updateTag(tag) {
  closeEditDialog()
  return tagService.updateTag(tag)
      .then(loadTags)
}

function addTag(tag) {
  closeAddDialog()
  return tagService.addTag(tag)
      .then(loadTags)
}

function closeEditDialog() {
  displayEditDialog.value = false
}

function openAddDialog() {
  displayAddDialog.value = true
}

function closeAddDialog() {
  displayAddDialog.value = false
}
</script>

<style scoped></style>
