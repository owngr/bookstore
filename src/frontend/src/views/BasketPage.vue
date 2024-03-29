<template>
  <h1>{{ i18n.global.t("basket") }}</h1>
  <div class="flex flex-wrap gap-3 p-3">
    <label class="ml-2" for="displayOpenBaskets">{{
      i18n.global.t("displayOpenBaskets")
    }}</label>
    <InputSwitch
      v-model="displayOpenBaskets"
      input-id="displayOpenBaskets"
      @input="loadBaskets()"
    />
  </div>
  <PAccordion :active-index="0">
    <AccordionTab
      v-for="basket in baskets"
      :key="basket.distributor"
      :header="basket.title"
    >
      <basket-accordion :basket="basket" @close-basket="closeBasket" />
    </AccordionTab>
  </PAccordion>
</template>

<script lang="ts" setup>
import { inject, ref } from "vue";
import basketService from "@/service/BasketService";
import BasketAccordion from "@/components/BasketAccordion.vue";
import i18n from "@/i18n";
import { BasketDto } from "@/models/BasketDto";
import { Emitter } from "mitt";
import { MessageEvent } from "@/classes/Message";

const baskets = ref<BasketDto[]>([]);
const displayOpenBaskets = ref(true);

const emitter: Emitter<MessageEvent> = inject(
  "emitter"
) as Emitter<MessageEvent>;
loadBaskets();

function defineHeaders(): void {
  baskets.value.forEach((b) => {
    if (b.dateClosed !== "") {
      b.title =
        b.distributor + " " + i18n.global.t("closedOn") + " " + b.dateClosed;
    } else {
      b.title = b.distributor + " " + i18n.global.t("open");
    }
  });
}

function loadBaskets(): void {
  basketService
    .getBaskets(displayOpenBaskets.value)
    .then((data) => {
      baskets.value = data;
      defineHeaders();
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("couldNotLoadBasketsMessage", { error: e }),
      })
    );
}

function closeBasket(basketId: string) {
  basketService
    .closeBasket(basketId)
    .then(() => {
      emitter.emit("notify", {
        severity: "success",
        content: i18n.global.t("basketClosedMessage"),
      });
      loadBaskets();
    })
    .catch((e) =>
      emitter.emit("notify", {
        severity: "error",
        content: i18n.global.t("couldNotCloseBasketMessage", { error: e }),
      })
    );
}
</script>

<style scoped></style>