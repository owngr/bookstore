<template>
  <MenuBar :model="items"/>
</template>
<script setup>

import i18n from "@/i18n";

const items = [
  {
    label: i18n.global.t('homePage'),
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
    label: i18n.global.t('sellBooks'),
    to: '/book/sell',
  },
  {
    label: i18n.global.t('sales'),
    to: '/sales',
  },
  {
    label: i18n.global.t('more'),
    items: [
      {
        label: i18n.global.t('inventory'),
        to: '/inventory',
      },
      {
        label: i18n.global.t('basket'),
        to: '/basket',
      },
      {
        label: i18n.global.t('tags'),
        to: '/tag',
      }
    ]
  },
]
</script>

<style scoped>

</style>