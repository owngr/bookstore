<template>
  <div class="center">
    <table>
      <tr v-for="button in buttons" :key="button.text">
        <td colspan="2">
          <PButton aria-labelledby="Cash" class="p-button-lg button" @click="onAddFreeSale(button.title)">
            {{ $t(button.text) }}
          </PButton>
        </td>
      </tr>
      <tr>
        <td>
          <InputText
              id="title"
              v-model="freeTextTitle"
              name="title"
              type="text"
          />
        </td>
        <td>
          <PButton
              :disabled="freeTextTitle === null"
              aria-labelledby="Cash"
              class="p-button-lg"
              @click="onAddFreeSale(freeTextTitle)"
          >
            {{ $t('addFreeText') }}
          </PButton>
        </td>
      </tr>

    </table>
  </div>

</template>

<script setup>

import {defineEmits, ref} from "vue";

const emit = defineEmits(['add-free-sale'])

const freeTextTitle = ref(null)

const buttons = [
  {text: 'addFanzine', title: 'fanzine'},
  {text: 'addVoucher', title: 'bon cadeau'},
  {text: 'addPostcard', title: 'Carte postale'},
  {text: 'addSecondHandBook', title: 'Livre d\'occasion'},
  {text: 'addGame', title: 'Jeux'},
  {text: 'addEvent', title: 'Événement'},
  {text: 'addExpo', title: 'Exposition'},
]

function onAddFreeSale(title) {
  const sale = {
    isbn: null,
    title: title,
    quantity: 1,
    fullPrice: 0,
    price: 0,
    new: false,
  }
  emit('add-free-sale', sale)
}


</script>

<style scoped>
.button {
  width: 100%;
}

.center {
  width: 100%;
  display: flex;
  justify-content: center;
}

</style>