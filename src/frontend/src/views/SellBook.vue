<template>
  <h1>Vendre un livre</h1>
  <Message v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{ msg.content }}
  </Message>

  <SelectButton v-model="selectedPaymentOption" :option="['test','aa']"/>
  <Dialog v-model:visible="displayPaymentDialog" :draggable="false" header="Choix du système de payement"
          :style="{width: '50vw'}">
    <div style="text-align: center">
      <Button aria-labelledby="Cash" class="p-button-lg" @click="sellBooks('CASH')">Cash</Button>
      <Button aria-labelledby="Card" class="p-button-lg" @click="sellBooks('CARD')">Carte</Button>
      <Button aria-labelledby="Twint" class="p-button-lg" @click="sellBooks('TWINT')">Twint</Button>
    </div>
  </Dialog>
  <IsbnSearch
      :reset-on-found="true"
      api-path="/api/stock/"
      @book="createSaleEntry"
      @message="messages.push($event)"
  />
  <Button
      label="Ajout d'un fanzine"
      @click="addFanzine()"
  />
  <form
      id="app"
      @submit.prevent="processForm"
  >
    <DataTable
        :value="sales"
        edit-mode="cell"
        @cell-edit-complete="onCellEditComplete"
    >
      <Column field="isbn" header="ISBN"></Column>
      <Column field="title" header="Titre">
        <template #body="slotProps">
          <div :class="titleClass(slotProps.data)">
            {{ slotProps.data.title }}
          </div>
        </template>
      </Column>
      <Column field="quantity" header="Quantité"></Column>
      <Column key="price" field="fullPrice" header="Prix">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" autofocus/>
        </template>
      </Column>
      <Column field="delete">
        <template #body="slotProps">
          <Button icon="pi pi-times" class="p-button-rounded p-button-danger" aria-label="Cancel"
                  @click="deleteRow(slotProps)"/>
        </template>
      </Column>


      <!--      <Column :row-editor="true" style="width:10%; min-width:8rem" body-style="text-align:center"/>-->

      <ColumnGroup type="footer">
        <Row>
          <Column footer="Réduction" :colspan="3"/>
          <Column :colspan="1">
            <template #footer>
              <InputNumber
                  id="priceReductionPercentage"
                  v-model="priceDiscount"
                  name="priceReductionPercentage"
                  :show-buttons="false"
                  suffix="%"
              />
            </template>
          </Column>
        </Row>
        <Row>
          <Column footer="Total:" :colspan="3"/>
          <Column :footer="sum.toFixed(2) + ' CHF'"/>
        </Row>
      </ColumnGroup>
    </DataTable>

    <Button
        :label="submitButtonText"
        type="submit"
        value="submit"
    />
  </form>
</template>
<script setup>
import {ref, defineProps, computed} from "vue";
import IsbnSearch from "@/components/IsbnSearch";
import StockService from "@/service/StockService";

defineProps({
  submitButtonText: {
    type: String,
    default: "Vendre",
  },
})

const messages = ref([])
const sales = ref([])
let displayPaymentDialog = ref(false)
const selectedPaymentOption = ref(null)
let priceDiscount = ref(0)
let sum = computed(() => priceSum())

function titleClass(data) {
  return [
    {
      'secondhand': data.new === false,
    }
  ]
}

function addFanzine() {
  sales.value.push({
    isbn: null,
    title: "fanzine",
    quantity: 1,
    fullPrice: 0,
    price: 0,
    new: false,
  })
}

function deleteRow(sale) {
  const saleIndex = sales.value.indexOf(sale)
  sales.value.splice(saleIndex, 1)
}

function createSaleEntry(book) {
  sales.value.push({
    isbn: book.isbn,
    title: book.title,
    quantity: 1,
    fullPrice: parseFloat(book.price ?? 0),
    price: parseFloat(book.price ?? 0),
    new: true,
  })
}

function adaptBooksPrice(reducedSum) {
  let reducedBooks = sales.value.filter(sale => sale.new)
  let sum = 0;
  for (let i = 0; i < reducedBooks.length - 1; i++) {
    const orignalPrice = reducedBooks[i].fullPrice
    let reducedPrice = orignalPrice - (orignalPrice * priceDiscount.value / 100)
    reducedPrice = Math.round(reducedPrice * 100) / 100.0
    reducedBooks[i].price = reducedPrice
    sum += reducedPrice
  }
  if (reducedBooks.length > 0) {
    reducedBooks[reducedBooks.length - 1].price = reducedSum - sum
  }
}

function priceSum() {
  let reducedSum = sales.value.filter(sale => sale.new).reduce((sum, sale) => sum + sale.fullPrice, 0)
  // round it up
  reducedSum = Math.ceil((reducedSum - (reducedSum * priceDiscount.value / 100)) * 20) / 20
  let fullpriceSum = sales.value.filter(sale => !sale.new).reduce((sum, sale) => sum + sale.fullPrice, 0)
  let sum = reducedSum + fullpriceSum
  adaptBooksPrice(reducedSum)
  return sum
}

function priceSumWithoutDiscount() {
  return sales.value.reduce((sum, sale) => sum + sale.fullPrice, 0)

}

function onCellEditComplete(event) {
  console.debug("OnCellEditComplete")
  console.log(event)
  let {data, index, newValue, field} = event;
  if (parseFloat(newValue) >= 0) {
    data[field] = parseFloat(newValue);
    sales.value[index].new = false;
    sales.value[index].price = parseFloat(newValue);
  } else {
    event.preventDefault();
  }
}

function processForm() {
  return StockService.getMissingBooks({"sales": sales.value})
      .then(res => {
        if (!res.ok) {
          return res.json()
        }
        return true
      })
      .then(res => {
        if (res === true) {
          displayPaymentDialog.value = true
        } else if (res.length !== 0) {
          res.forEach(stockEntry => {
            messages.value.push({
              severity: 'error',
              content: `Le livre ${stockEntry.title} n'a que ${stockEntry.amount} exemplaire(s) en stock`
            })
          })
        }
      })
      .catch(e => {
        console.debug(e)
      })
}


function sellBooks(paymentOption) {
  // computed copy cannot be serialized
  const sumCopy = priceSum()
  return StockService.sellBooks({
    "sales": sales.value,
    "paymentMethod": paymentOption,
    "priceWithDiscount": sumCopy,
    "priceDiscountPercent": priceDiscount.value,
    "priceWithoutDiscount": priceSumWithoutDiscount()
  })
      .then(res => {
        if (res.ok) {
          displayPaymentDialog.value = false
          messages.value.push({
            severity: 'success',
            content: 'La vente a été effectuée avec succès'
          })
          sales.value = []
        }
      })
      .catch(e => {
        messages.value.push({
          severity: 'error',
          content: e
        })
      })
}

</script>

<style scoped lang="css">
.p-button {
  margin: .2rem;
}

.secondhand {
  background-color: yellow;
}

</style>