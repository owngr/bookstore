<template>
  <label class="form-label" for="authors">Auteur·ices</label>
  <div v-for="(author, index) in authors" v-bind:key="index" id="authors">
    <InputText v-model="author.value"/>
    <Button @click="deleteAuthorFunction(index)" v-if="index !== 0" label="X" />
  </div>
  <Button @click="addAuthorFunction" label="nouve̛l·le auteur·rice"/>
</template>
<script>
export default {
  name: 'AuthorForm',
  props: {
    addAuthorFunction: {},
    deleteAuthorFunction: {},
    authors: {},
  }
}
</script>
