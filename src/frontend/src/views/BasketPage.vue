<template>
<h1>{{ $t('basket') }}</h1>
</template>

<script setup>
import basketService from "@/service/BasketService";

basketService.getOpenBaskets()
    .then(data => {
      console.debug(data)
    })
</script>

<style scoped>

</style>