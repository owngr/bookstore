<template>
  <PageHeader></PageHeader>
  <AppNotification/>
  <router-view></router-view>
</template>

<script lang="ts">
import PageHeader from './components/PageHeader.vue';
import AppNotification from "./components/AppNotification.vue";

export default {
  name: 'App',
  components: {
    AppNotification,
    PageHeader,
  }
}
</script>

<style>

</style>
