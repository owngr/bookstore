<template>
  <h1>Vendre un livre</h1>
  <Message v-for="msg of messages" :key="msg.content" :sticky="false" :severity="msg.severity">{{ msg.content }}
  </Message>

  <SelectButton v-model="selectedPaymentOption" :option="['test','aa']" />
  <Dialog v-model:visible="displayPaymentDialog" :draggable="false" header="Choix du système de payement" :style="{width: '50vw'}">
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
      <Column field="title" header="Titre"></Column>
      <Column field="quantity" header="Quantité"></Column>
      <Column key="price" field="price" header="Prix">
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

      <Column :row-editor="true" style="width:10%; min-width:8rem" body-style="text-align:center"/>
      <ColumnGroup type="footer">
        <Row>
          <Column footer="Totals:" :colspan="3"/>
          <Column :footer="priceSum()"/>
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
import {ref, defineProps} from "vue";
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


function deleteRow(sale) {
  const saleIndex = sales.value.indexOf(sale)
  sales.value.splice(saleIndex, 1)
}

function createSaleEntry(book) {
  sales.value.push({isbn: book.isbn, title: book.title, quantity: 1, price: parseFloat(book.price ?? 0)})
}

function priceSum() {
  const sum = sales.value.reduce((sum, sale) => sum + sale.price,
      0
  )
  console.log("sum " + sum)
  console.log(sum)
  return sum
}

function onCellEditComplete(event) {
  console.debug("OnCellEditComplete")
  let {data, newValue, field} = event;
  if (parseFloat(newValue) >= 0) {
    data[field] = parseFloat(newValue);
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
        }else if (res.length !== 0) {
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
  console.debug(paymentOption)
  return StockService.sellBooks({"sales": sales.value, "paymentMethod": paymentOption})
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

<style scoped>
.p-button {
  margin: .2rem;
}

</style>