<template>
  <InputText
      id="isbn"
      ref="isbnsearchRef"
      v-model="isbn"
      class="form-control"
      type="number"
      name="isnbn"
      minlength="10"
      maxlength="13"
      :disabled="disabled"
      @keyup.enter="searchIsbn"
  />
</template>

<script setup>
import {ref, defineProps, defineEmits, onMounted} from "vue"


const emit = defineEmits(['book'])

const props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
  apiPath: {
    type: String,
    default: "/api/book/ISBN?isbn="
  },
  resetOnFound: {
    type: Boolean,
    default: false,
  },
  initIsbn: {
    type: String,
    default: "",
  },
})
console.log(props.initIsbn)
const isbnsearchRef = ref(null)
const isbn = ref(props.initIsbn)

// we set the focus on isbn if no isbn is present
onMounted(() => {
  if (!isbn.value) {
    isbnsearchRef.value.$el.focus()
  }
})


const searchIsbn = () => {
  console.debug("fetching data")
  console.log(isbn.value)
  if (isbn.value.length === 13) {
    fetch(props.apiPath + isbn.value)
        .then((response) => response.text())
        .then((data) => {
          if (props.resetOnFound) {
            isbn.value = ""
          }
          emit('book', JSON.parse(data))
        })
        .catch((e) => this.$emit('message', {severity: 'warn', content: `Le livre n'a pas pu être trouvé {}` + e}))
  }
}

</script>

<style scoped>

</style>