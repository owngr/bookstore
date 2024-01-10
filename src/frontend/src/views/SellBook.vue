<template>
  <h1>{{ i18n.global.t("sellBooks") }}</h1>

  <SelectButton v-model="selectedPaymentOption" :option="['test', 'aa']" />
  <PDialog
    v-model:visible="displayMissingBookDialog"
    :draggable="false"
    :header="i18n.global.t('missingBooks')"
    :style="{ width: '50vw' }"
  >
    <MissingBooks
      :missing-books="missingBooks"
      :sales="sales"
      @canceled="displayMissingBookDialog = false"
      @added-books="processForm"
    />
  </PDialog>
  <PDialog
    v-model:visible="displayPaymentDialog"
    :draggable="false"
    :header="i18n.global.t('choosePaymentOption')"
    :style="{ width: '50vw' }"
  >
    <Payment :price="priceSum()" @sell="sellBooks" />
  </PDialog>
  <PDialog
    v-model:visible="displayFreeSaleDialog"
    :draggable="false"
    :header="i18n.global.t('chooseFreeSaleCategory')"
    :style="{ width: '50vw' }"
  >
    <FreeSale @add-free-sale="addFreeSale" />
  </PDialog>
  <IsbnSearch
    :reset-on-found="true"
    api-path="/api/stock/"
    @book="createSaleEntry"
  />
  <PButton :label="i18n.global.t('addFreeSale')" @click="onAddFreeSale()" />
  <form id="app" @submit.prevent="processForm">
    <DataTable
      :value="sales"
      edit-mode="cell"
      @cell-edit-complete="onCellEditComplete"
    >
      <PColumn :header="i18n.global.t('isbn')" field="isbn"></PColumn>
      <PColumn :header="i18n.global.t('title')" field="title">
        <template #body="slotProps">
          <div :class="titleClass(slotProps.data)">
            {{ slotProps.data.title }}
          </div>
        </template>
      </PColumn>
      <PColumn :header="i18n.global.t('quantity')" field="quantity"></PColumn>
      <PColumn key="price" :header="i18n.global.t('price')" field="fullPrice">
        <template #editor="{ data, field }">
          <InputNumber
            v-model="data[field]"
            :show-buttons="false"
            autofocus
            currency="CHF"
            mode="currency"
          />
        </template>
      </PColumn>
      <PColumn field="delete">
        <template #body="slotProps">
          <PButton
            aria-label="Cancel"
            class="p-button-rounded p-button-danger"
            icon="pi pi-times"
            @click="deleteRow(slotProps)"
          />
        </template>
      </PColumn>

      <!--      <PColumn :PRow-editor="true" style="width:10%; min-width:8rem" body-style="text-align:center"/>-->

      <ColumnGroup type="footer">
        <PRow>
          <PColumn :colspan="3" :footer="i18n.global.t('discount')" />
          <PColumn :colspan="1">
            <template #footer>
              <InputNumber
                id="priceReductionPercentage"
                v-model="priceDiscount"
                :show-buttons="false"
                name="priceReductionPercentage"
                suffix="%"
              />
            </template>
          </PColumn>
        </PRow>
        <PRow>
          <PColumn :colspan="3" :footer="i18n.global.t('total') + ':'" />
          <PColumn :footer="sum.toFixed(2) + ' CHF'" />
        </PRow>
      </ColumnGroup>
    </DataTable>

    <PButton :label="submitButtonText" type="submit" value="submit" />
  </form>
</template>
<script lang="ts" setup>
import { computed, defineProps, inject, ref } from "vue";
import IsbnSearch from "@/components/IsbnSearch";
import Payment from "@/components/PaymentDialog";
import StockService from "@/service/StockService";
import i18n from "@/i18n";
import MissingBooks from "@/components/MissingBooks";
import FreeSale from "@/components/FreeSale";
import { PaymentOptionDto, SaleDto } from "@/models/SaleDto";
import { ScraperBook } from "@/models/ScraperBook";
import { Emitter } from "mitt";
import {MessageEvent} from "@/classes/Message";

defineProps({
  submitButtonText: {
    type: String,
    default: i18n.global.t("sell"),
  },
});

const sales = ref<SaleDto[]>([]);
let displayPaymentDialog = ref<boolean>(false);
const displayMissingBookDialog = ref<boolean>(false);
const displayFreeSaleDialog = ref<boolean>(false);
const selectedPaymentOption = ref(null);
let missingBooks = ref<ScraperBook[]>([]);
let priceDiscount = ref(0);
let sum = computed(() => priceSum());
const emitter: Emitter<MessageEvent> = inject("emitter") as Emitter<MessageEvent>;

function titleClass(data: SaleDto) {
  return [
    {
      secondhand: !data.new,
    },
  ];
}

function addFreeSale(sale: SaleDto) {
  sales.value.push(sale);
  displayFreeSaleDialog.value = false;
}

function deleteRow(sale: SaleDto) {
  const sale2: SaleDto = Object.assign(new SaleDto(), sale);
  const saleIndex = sales.value.findIndex((s: SaleDto) => sale2.isEqual(s));
  sales.value.splice(saleIndex, 1);
}

function createSaleEntry(book: ScraperBook) {
  sales.value.push({
    isbn: book.isbn,
    title: book.title,
    quantity: 1,
    fullPrice: book.price ?? 0,
    price: book.price ?? 0,
    new: true,
    originalBook: book,
  });
}

function adaptBooksPrice(reducedSum: number) {
  let reducedBooks = sales.value.filter((sale) => sale.new);
  let sum = 0;
  for (let i = 0; i < reducedBooks.length - 1; i++) {
    const orignalPrice = reducedBooks[i].fullPrice;
    let reducedPrice =
      orignalPrice - (orignalPrice * priceDiscount.value) / 100;
    reducedPrice = Math.round(reducedPrice * 100) / 100.0;
    reducedBooks[i].price = reducedPrice;
    sum += reducedPrice;
  }
  if (reducedBooks.length > 0) {
    reducedBooks[reducedBooks.length - 1].price = reducedSum - sum;
  }
}

function priceSum(): number {
  let reducedSum = sales.value
    .filter((sale) => sale.new)
    .reduce((sum, sale) => sum + sale.fullPrice, 0);
  // round it up
  reducedSum =
    Math.round((reducedSum - (reducedSum * priceDiscount.value) / 100) * 20) /
    20;
  let fullpriceSum = sales.value
    .filter((sale) => !sale.new)
    .reduce((sum, sale) => sum + sale.fullPrice, 0);
  let sum = reducedSum + fullpriceSum;
  adaptBooksPrice(reducedSum);
  return sum;
}

function priceSumWithoutDiscount(): number {
  return sales.value.reduce((sum, sale) => sum + sale.fullPrice, 0);
}

function onCellEditComplete(event) {
  console.debug("OnCellEditComplete");
  console.log(event);
  let { data, index, newValue, field } = event;
  if (parseFloat(newValue) >= 0) {
    data[field] = parseFloat(newValue);
    sales.value[index].price = parseFloat(newValue);
  } else {
    event.preventDefault();
  }
}

function onAddFreeSale() {
  displayFreeSaleDialog.value = true;
}

function processForm() {
  // As we might just have the missing books dialog we make sure that it's closed first
  displayMissingBookDialog.value = false;
  return StockService.getMissingBooks({ sales: sales.value })
    .then((res) => {
      if (!res.ok) {
        return res.json();
      }
      return true;
    })
    .then((res) => {
      if (res === true) {
        displayPaymentDialog.value = true;
        // Some books are missing and we allow the user to add them before selling them
      } else if (res.length !== 0) {
        missingBooks.value = res;
        displayMissingBookDialog.value = true;
      }
    })
    .catch((e) => {
      console.debug(e);
    });
}

function sellBooks(paymentOptions: PaymentOptionDto[]) {
  // computed copy cannot be serialized
  const sumCopy = priceSum();
  return StockService.sellBooks({
    sales: sales.value,
    paymentMethod: paymentOptions,
    priceWithDiscount: sumCopy,
    priceDiscountPercent: priceDiscount.value,
    priceWithoutDiscount: priceSumWithoutDiscount(),
  })
    .then((res) => {
      if (res.ok) {
        displayPaymentDialog.value = false;
        emitter.emit("notify", {
          severity: "success",
          content: i18n.global.t("saleDoneWithSuccessMessage"),
        });
        sales.value = [];
      }
    })
    .catch((e) => {
      emitter.emit("notify", {
        severity: "error",
        content: e,
      });
    });
}
</script>

<style lang="css" scoped>
.p-button {
  margin: 0.2rem;
}

.secondhand {
  background-color: yellow;
}
</style>