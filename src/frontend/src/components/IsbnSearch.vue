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
      @focusin="emitPreventSubmit(true)"
      @focusout="emitPreventSubmit(false)"
  />
</template>

<script setup>
import {ref, defineProps, defineEmits, onMounted, inject} from "vue"
import i18n from "@/i18n";


const emit = defineEmits(['book', 'preventSubmit'])
const emitter = inject('emitter')

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
const preventSubmit = ref(false)

// we set the focus on isbn if no isbn is present
onMounted(() => {
  if (!isbn.value) {
    isbnsearchRef.value.$el.focus()
  }
})


const searchIsbn = () => {
  console.debug("fetching data")
  console.log(isbn.value)
  if (!preventSubmit.value) {
    return
  }
  if (isbn.value && isbn.value.length === 13) {
    fetch(props.apiPath + isbn.value)
        .then((response) => response.text())
        .then((data) => {
          if (props.resetOnFound) {
            isbn.value = ""
          }
          emit('book', JSON.parse(data))
        })
        .catch((e) => emitter.emit('notify', {severity: 'warn', content: i18n.global.t('bookNotFoundMessage', {error: e})}))
  }
}

const emitPreventSubmit = (prevent) => {
  preventSubmit.value = prevent
  emit('preventSubmit', prevent)
}

</script>

<style scoped>

</style>