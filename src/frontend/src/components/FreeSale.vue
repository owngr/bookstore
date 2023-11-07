<template>
  <div class="center">
    <table>
      <tr v-for="button in buttons" :key="button.text">
        <td colspan="2">
          <PButton
            aria-labelledby="Cash"
            class="p-button-lg button"
            @click="onAddFreeSale(button.title)"
          >
            {{ i18n.global.t(button.text) }}
          </PButton>
        </td>
      </tr>
      <tr>
        <td>
          <InputText
            id="title"
            v-model="freeTextTitle"
            name="title"
            type="text"
          />
        </td>
        <td>
          <PButton
            :disabled="freeTextTitle === null"
            aria-labelledby="Cash"
            class="p-button-lg"
            @click="onAddFreeSale(freeTextTitle)"
          >
            {{ i18n.global.t("addFreeText") }}
          </PButton>
        </td>
      </tr>
    </table>
  </div>
</template>

<script lang="ts" setup>
import { defineEmits, ref } from "vue";
import i18n from "@/i18n";
import { SaleDto } from "@/models/SaleDto";

const emit = defineEmits(["add-free-sale"]);

const freeTextTitle = ref(null);

const buttons = [
  { text: "addFanzine", title: "fanzine" },
  { text: "addVoucher", title: "bon cadeau" },
  { text: "addPostcard", title: "Carte postale" },
  { text: "addSecondHandBook", title: "Livre d'occasion" },
  { text: "addGame", title: "Jeux" },
  { text: "addEvent", title: "Événement" },
  { text: "addExpo", title: "Exposition" },
];

function onAddFreeSale(title: string) {
  const sale = new SaleDto();
  sale.isbn = null;
  sale.title = title;
  sale.quantity = 1;
  sale.fullPrice = 0;
  sale.price = 0;
  sale.new = false;
  emit("add-free-sale", sale);
}
</script>

<style scoped>
.button {
  width: 100%;
}

.center {
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>