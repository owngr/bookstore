<template>
  <Menubar :model="items" />
</template>

<script>
export default {
  name: "Header.vue",
  data() {
    return {
      items: [
        {
          label:'Home',
          to: '/',
        },
        {
          label:'Stock',
          to: '/stock',
        },
        {
          label:'Ajout de livres',
          to: '/book/add',
        }
      ]
    }
  }

}
</script>

<style scoped>

</style>