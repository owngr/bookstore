<template>
  <MenuBar :model="items"/>
</template>
<script setup>

import i18n from "@/i18n";

const items = [
  {
    label: i18n.global.t('home'),
    to: '/',
  },
  {
    label: i18n.global.t('stock'),
    to: '/stock',
  },
  {
    label: i18n.global.t('addBooks'),
    to: '/book/add',
  },
  {
    label: i18n.global.t('inventory'),
    to: '/inventory',
  },
  {
    label: i18n.global.t('sellBooks'),
    to: '/book/sell',
  },
  {
    label: i18n.global.t('sales'),
    to: '/sales',
  }
]
</script>

<style scoped>

</style>