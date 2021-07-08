<template>
  <Header></Header>
  <router-view></router-view>
  <HelloWorld />
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import "bootstrap/dist/css/bootstrap.min.css";
import Header from './components/Header.vue'

export default {
  name: 'App',
  components: {
    Header,
    HelloWorld
  }
}
</script>

<style>

</style>