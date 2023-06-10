<template>
  <PMessage v-for="msg of messages" :key="msg.content" :severity="msg.severity" :sticky="msg.severity === 'error'">{{ msg.content }}
  </PMessage>
</template>

<script setup>
import {inject, ref} from "vue";

const messages = ref([])

const emitter = inject('emitter')

//TODO find a way to not let the list grow without end
function handleNotification(message) {
  messages.value.push(message)
}

emitter.on('notify', message => handleNotification(message))



</script>

<style scoped>

</style>