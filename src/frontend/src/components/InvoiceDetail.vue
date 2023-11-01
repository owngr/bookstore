<template>
  <div style="text-align: center">
    <DataTable
        :value="props.saleList.sales">

      <PColumn :header="$t('isbn')" field="isbn"/>
      <PColumn :header="$t('description')" field="title"/>
      <PColumn :header="$t('new')" field="new">
        <template #body="slotProps">
          {{ $t(slotProps.data.new.toString()) }}
        </template>
      </PColumn>
      <PColumn :header="$t('price')" field="price"/>
      <ColumnGroup type="footer">
        <PRow v-for="row in footer" :key="row.translateKey">
          <PColumn :colspan="3" :footer="$t(row.translateKey)" style="font-weight: lighter"/>
          <PColumn :colspan="1" :footer="row.value"/>
        </PRow>
      </ColumnGroup>

    </DataTable>
  </div>


</template>

<script setup>

import {computed} from "vue";
import {defineProps} from "vue"

const props = defineProps({
  saleList: {
    type: Object,
    default: () => {
    }
  }
})

// primevue doesn't support mixing v-for with static rows
const footer = computed(() => {
  const rows = []
  rows.push({translateKey: "total", value: props.saleList.priceWithoutDiscount})
  if (props.saleList.priceDiscountPercent !== 0) {
    rows.push({translateKey: "priceDiscountPercent", value: '- ' + props.saleList.priceDiscountPercent + '%'})
    rows.push({translateKey: "priceWithDiscount", value: props.saleList.priceWithDiscount})
  }
  props.saleList.paymentMethod.forEach(p => {
    rows.push({translateKey: p.value, value: p.price})
  })
  return rows
})

console.debug(props.saleList)

</script>

<style scoped>

</style>