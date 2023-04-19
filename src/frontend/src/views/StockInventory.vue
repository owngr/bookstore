<template>
  <h1>{{ $t('inventory') }}</h1>
  <table>
    <tr>
      <td>
        <label class="form-label" for="bookAmount">{{ $t('bookAmount') }}</label>
      </td>
      <td>
        <InputText
            id="bookAmount"
            v-model="inventory.bookAmount"
            type="number"
            :disabled="true"
        />
      </td>
      <td>
        <label class="form-label" for="moneySum">{{ $t('moneySum') }}</label>
      </td>
      <td>
        <InputText
            id="bookAmount"
            v-model="inventory.moneySum"
            type="number"
            :disabled="true"
        />
      </td>
    </tr>
  </table>
  <PButton :label="$t('deleteStock')" @click="deleteStock"/>

</template>

<script>
import InventoryService from "../service/InventoryService";
import StockService from "../service/StockService";
import i18n from "@/i18n";
import {inject} from "vue";

export default {
  name: "StockInventory",
  data() {
    return {
      inventory: {
        bookAmount: 0,
        moneySum: 0,
      },
      emitter: inject('emitter')
    }
  },

  mounted() {
    this.fetchInventory();
  },
  methods: {
    fetchInventory() {
      InventoryService.getInventory().then(data => {
        this.inventory = data
      });
    },
    deleteStock() {
      StockService.deleteStock().then(() => {
        this.emitter.emit('notify',{severity: 'success', content: i18n.global.t('stockHasBeenDeletedMessage')})
      })
    }
  }
}
</script>

<style scoped>

</style>