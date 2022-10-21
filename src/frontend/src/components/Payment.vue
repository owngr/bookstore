<template>
  <div style="text-align: center">
    <table>
      <tr v-for="paymentOption in paymentOptions" :key="paymentOption.name">
        <td>
          <Button aria-labelledby="Cash" class="p-button-lg" @click="sellWithSinglePaymentOption(paymentOption.value)">
            {{ paymentOption.name }}
          </Button>
        </td>
        <td>
          <InputNumber v-model="paymentOption.price" mode="currency" currency="CHF"/>
        </td>
      </tr>
      <tr>
        <td>
          <label class="form-label" for="price">Reste</label>
        </td>
        <td>
          <InputNumber v-model="remainingMoney" mode="currency" currency="CHF" :readonly="true"/>
        </td>
      </tr>
    </table>
    <Button :disabled="remainingMoney !== 0" @click="sellWithManyPaymentOptions">Valider</Button>
  </div>
</template>

<script setup>
import {computed} from "vue";
import {defineEmits, defineProps, ref} from "vue";

const props = defineProps({
  price: {
    type: Number,
    default: 0,
  },
})

const paymentOptions = ref([
  {
    name: "Cash",
    value: "CASH",
    price: 0,
  },
  {
    name: "Carte",
    value: "CARD",
    price: 0,
  },
  {
    name: "Twint",
    value: "TWINT",
    price: 0,
  },
  {
    name: "Bon",
    value: "VOUCHER",
    price: 0,
  },
])

const emit = defineEmits(['sell'])

const sellWithSinglePaymentOption = (paymentOption) => {
  sell([{value: paymentOption, price: props.price}])
}

const sellWithManyPaymentOptions = () => {
  const payments = paymentOptions.value
      .filter(po => po.price !== 0)
      // eslint-disable-next-line no-unused-vars
      .map(({name, ...rest}) => rest)
  sell(payments)
}

const sell = (payments) => {
  emit('sell', payments)
}
const remainingMoney = computed(() => {
  return props.price - paymentOptions.value.reduce((sum, po) => sum + po.price, 0)
})

</script>

<style scoped>

</style>