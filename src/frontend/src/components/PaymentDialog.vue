<template>
  <div style="text-align: center">
    <table>
      <tr v-for="paymentOption in paymentOptions" :key="paymentOption.name">
        <td>
          <PButton
            aria-labelledby="Cash"
            class="p-button-lg"
            @click="sellWithSinglePaymentOption(paymentOption.value)"
          >
            {{ i18n.global.t(paymentOption.value) }}
          </PButton>
        </td>
        <td>
          <InputNumber
            v-model="paymentOption.price"
            currency="CHF"
            mode="currency"
          />
        </td>
      </tr>
      <tr>
        <td>
          <label class="form-label" for="price">Reste</label>
        </td>
        <td>
          <InputNumber
            v-model="remainingMoney"
            :readonly="true"
            currency="CHF"
            mode="currency"
          />
        </td>
      </tr>
    </table>
    <PButton
      :disabled="remainingMoney !== 0"
      @click="sellWithManyPaymentOptions"
      >Valider</PButton
    >
  </div>
</template>

<script lang="ts" setup>
import { computed, defineEmits, defineProps, ref } from "vue";
import i18n from "@/i18n";

const props = defineProps({
  price: {
    type: Number,
    default: 0,
  },
});

const paymentOptions = ref([
  {
    value: "CASH",
    price: 0,
  },
  {
    value: "CARD",
    price: 0,
  },
  {
    value: "TWINT",
    price: 0,
  },
  {
    value: "VOUCHER",
    price: 0,
  },
]);

const emit = defineEmits(["sell"]);

function sellWithSinglePaymentOption(paymentOption): void {
  sell([{ value: paymentOption, price: props.price }]);
}

function sellWithManyPaymentOptions(): void {
  const payments = paymentOptions.value
    .filter((po) => po.price !== 0)
    // eslint-disable-next-line no-unused-vars,@typescript-eslint/no-unused-vars
    .map(({ name, ...rest }) => rest);
  sell(payments);
}

function sell(payments): void {
  emit("sell", payments);
}

const remainingMoney = computed(() => {
  return (
    props.price - paymentOptions.value.reduce((sum, po) => sum + po.price, 0)
  );
});
</script>

<style scoped></style>