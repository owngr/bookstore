<template>
  <div v-for="(author, index) in authors" :key="index" class="m-1">
    <InputText v-model="author.value" class="w-10" />
    <PButton
      v-if="index !== 0"
      class="w-2 p-button-danger"
      label="X"
      @click="deleteAuthorFunction(index)"
    />
  </div>
  <PButton
    :label="i18n.global.t('newAuthor')"
    class="w-full"
    @click="addAuthorFunction"
  />
</template>
<script lang="ts" setup>
import { defineProps, ref } from "vue";
import i18n from "@/i18n";

defineProps<{
  addAuthorFunction: () => void;
  deleteAuthorFunction: (index: number) => void;
  authors: ref<string>;
}>();
</script>