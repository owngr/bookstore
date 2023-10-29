<template>
  <div v-for="(author, index) in authors" :key="index" class="m-1">
    <InputText v-model="author.value" class="w-10" />
    <PButton v-if="index !== 0" label="X" @click="deleteAuthorFunction(index)" class="w-2 p-button-danger"/>
  </div>
  <PButton :label="$t('newAuthor')" @click="addAuthorFunction" class="w-full"/>
</template>
<script setup>
import {defineProps} from "vue"
  defineProps({
    addAuthorFunction: {},
    deleteAuthorFunction: {},
    authors: {},
  })
</script>
