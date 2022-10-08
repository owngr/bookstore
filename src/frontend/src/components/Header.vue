<template>
  <Menubar :model="items"/>
</template>
<script setup>
const items = [
  {
    label: 'Home',
    to: '/',
  },
  {
    label: 'Stock',
    to: '/stock',
  },
  {
    label: 'Ajout de livres',
    to: '/book/add',
  },
  {
    label: 'Inventaire',
    to: '/inventory',
  },
  {
    label: "Vendre des livres",
    to: '/book/sell',
  },
  {
    label: "Ventes",
    to: '/sales',
  }
]
</script>

<style scoped>

</style>