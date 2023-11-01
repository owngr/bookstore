<template>
  <div v-for="(author, index) in authors" :key="index" class="m-1">
    <InputText v-model="author.value" class="w-10" />
    <PButton v-if="index !== 0" label="X" class="w-2 p-button-danger" @click="deleteAuthorFunction(index)"/>
  </div>
  <PButton :label="$t('newAuthor')" class="w-full" @click="addAuthorFunction"/>
</template>
<script setup>
import {defineProps} from "vue"
  defineProps({
    addAuthorFunction: {},
    deleteAuthorFunction: {},
    authors: {},
  })
</script>
