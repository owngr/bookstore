<template>
  <h1>{{ $t('inventory') }}</h1>
  <PMessage v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{msg.content}}</PMessage>
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

export default {
  name: "StockInventory",
  data() {
    return {
      inventory: {
        bookAmount: 0,
        moneySum: 0,
      },
      messages: []
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
        this.messages.push({severity: 'success', content: i18n.global.t('stockHasBeenDeletedMessage')})
      })
    }
  }
}
</script>

<style scoped>

</style>