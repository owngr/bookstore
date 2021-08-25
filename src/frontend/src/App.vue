<template>
  <Header></Header>
  <router-view></router-view>
  <HelloWorld />
</template>

<script>
import Header from './components/Header.vue';

export default {
  name: 'App',
  components: {
    Header,
  }
}
</script>

<style>

</style>