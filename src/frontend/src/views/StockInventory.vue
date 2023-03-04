<template>
  <h1>Inventaire</h1>
  <PMessage v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{msg.content}}</PMessage>
  <table>
    <tr>
      <td>
        <label class="form-label" for="bookAmount">Nombre de livres</label>
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
        <label class="form-label" for="moneySum">Somme d'argent</label>
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
  <PButton label="Supprimer le stock" @click="deleteStock"/>

</template>

<script>
import InventoryService from "../service/InventoryService";
import StockService from "../service/StockService";

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
        this.messages.push({severity: 'success', content: `Le stock a été supprimé`})
      })
    }
  }
}
</script>

<style scoped>

</style>