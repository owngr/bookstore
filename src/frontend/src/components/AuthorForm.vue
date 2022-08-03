<template>
  <div v-for="(author, index) in authors" :key="index">
    <InputText v-model="author.value"/>
    <Button v-if="index !== 0" label="X" @click="deleteAuthorFunction(index)" />
  </div>
  <Button label="nouve̛l·le auteur·rice" @click="addAuthorFunction"/>
</template>
<script setup>
import {defineProps} from "vue"
  defineProps({
    addAuthorFunction: {},
    deleteAuthorFunction: {},
    authors: {},
  })
</script>
