<template>
  <div style="text-align: center">
    <DataTable :value="props.saleList.sales">
      <PColumn :header="i18n.global.t('isbn')" field="isbn" />
      <PColumn :header="i18n.global.t('description')" field="title" />
      <PColumn :header="i18n.global.t('new')" field="new">
        <template #body="slotProps">
          {{ i18n.global.t(slotProps.data.new.toString()) }}
        </template>
      </PColumn>
      <PColumn :header="i18n.global.t('price')" field="price" />
      <ColumnGroup type="footer">
        <PRow v-for="row in footer" :key="row.translateKey">
          <PColumn
            :colspan="3"
            :footer="i18n.global.t(row.translateKey)"
            style="font-weight: lighter"
          />
          <PColumn :colspan="1" :footer="row.value" />
        </PRow>
      </ColumnGroup>
    </DataTable>
  </div>
</template>

<script lang="ts" setup>
import { computed, defineProps } from "vue";
import i18n from "@/i18n";
import { SaleList } from "@/models/SaleDto";

const props = defineProps({
  saleList: {
    type: SaleList,
  },
});

// primevue doesn't support mixing v-for with static rows
const footer = computed(() => {
  const rows = [];
  rows.push({
    translateKey: "total",
    value: props.saleList.priceWithoutDiscount,
  });
  if (props.saleList.priceDiscountPercent !== 0) {
    rows.push({
      translateKey: "priceDiscountPercent",
      value: "- " + props.saleList.priceDiscountPercent + "%",
    });
    rows.push({
      translateKey: "priceWithDiscount",
      value: props.saleList.priceWithDiscount,
    });
  }
  props.saleList.paymentMethod.forEach((p) => {
    rows.push({ translateKey: p.value, value: p.price });
  });
  return rows;
});

console.debug(props.saleList);
</script>

<style scoped></style>