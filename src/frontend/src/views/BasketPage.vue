<template>
  <h1>{{ $t('basket') }}</h1>
  <label for="displayOpenBaskets">{{ $t('displayOpenBaskets') }}</label>
  <InputSwitch v-model="displayOpenBaskets" input-id="displayOpenBaskets" @input="loadBaskets()"/>
  <PAccordion :activeIndex="0">
    <AccordionTab v-for="basket in baskets" :key="basket.distributor" :header="basket.title">
      <basket-accordion :basket="basket"/>
    </AccordionTab>
  </PAccordion>
</template>

<script setup>
import {ref} from 'vue';
import basketService from "@/service/BasketService";
import BasketAccordion from "@/components/BasketAccordion.vue";
import i18n from "@/i18n";

const baskets = ref([])
const displayOpenBaskets = ref(true)
loadBaskets()

function defineHeaders() {
  baskets.value.forEach(b => {
    if (b.dateClosed !== "") {
      b.title = b.distributor + " " + i18n.global.t('closedOn') + " " + b.dateClosed
    } else {
      b.title = b.distributor + " " + i18n.global.t('open')
    }
  })


}

function loadBaskets() {
  basketService.getBaskets(displayOpenBaskets.value)
      .then(data => {
        baskets.value = data
        defineHeaders()
      })
}

</script>

<style scoped>

</style>