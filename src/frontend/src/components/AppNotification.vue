<template>
  <PMessage
    v-for="msg of messages"
    :key="msg.content"
    :severity="msg.severity"
    :sticky="msg.severity === 'error'"
  >
    {{ msg.content }}
  </PMessage>
</template>

<script lang="ts" setup>
import { inject, ref } from "vue";
import {Mitt} from "mitt"
import {Message, MessageEvent} from "@/classes/Message";

const messages = ref<Message[]>([]);

const emitter: Mitt = inject("emitter");

//TODO find a way to not let the list grow without end
function handleNotification(message: Message) {
  messages.value.push(message);
}

emitter.on("notify", (message: Message) => handleNotification(message));
</script>

<style scoped></style>