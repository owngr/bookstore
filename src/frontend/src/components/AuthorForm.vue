<template>
  <div v-for="(author, index) in authors" :key="index">
    <InputText v-model="author.value"/>
    <PButton v-if="index !== 0" label="X" @click="deleteAuthorFunction(index)" />
  </div>
  <PButton :label="$t('newAuthor')" @click="addAuthorFunction"/>
</template>
<script setup>
import {defineProps} from "vue"
  defineProps({
    addAuthorFunction: {},
    deleteAuthorFunction: {},
    authors: {},
  })
</script>
