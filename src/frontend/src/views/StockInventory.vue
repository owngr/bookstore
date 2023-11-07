<template>
  <h1>{{ i18n.global.t("inventory") }}</h1>
  <table>
    <tr>
      <td>
        <label class="form-label" for="bookAmount">{{
          i18n.global.t("bookAmount")
        }}</label>
      </td>
      <td>
        <InputText
          id="bookAmount"
          v-model="inventory.bookAmount"
          :disabled="true"
          type="number"
        />
      </td>
      <td>
        <label class="form-label" for="moneySum">{{
          i18n.global.t("moneySum")
        }}</label>
      </td>
      <td>
        <InputText
          id="bookAmount"
          v-model="inventory.moneySum"
          :disabled="true"
          type="number"
        />
      </td>
    </tr>
  </table>
  <PButton :label="i18n.global.t('deleteStock')" @click="deleteStock" />
</template>

<script lang="ts" setup>
import InventoryService from "../service/InventoryService";
import StockService from "../service/StockService";
import i18n from "@/i18n";
import { inject, ref } from "vue";
import {Emitter} from "mitt";
import {MessageEvent} from "@/classes/Message";

const inventory: ref<Inventory> = ref({ bookAmount: 0, moneySum: 0 });

const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>

fetchInventory();

function fetchInventory() {
  InventoryService.getInventory().then((data) => {
    inventory.value = data;
  });
}

function deleteStock() {
  StockService.deleteStock().then(() => {
    emitter.emit("notify", {
      severity: "success",
      content: i18n.global.t("stockHasBeenDeletedMessage"),
    });
  });
}
</script>

<style scoped></style>