<template>
  <h1>Vendre un livre</h1>
  <Message v-for="msg of messages" :sticky="false" :severity="msg.severity" :key="msg.content">{{ msg.content }}
  </Message>

  <IsbnSearch
      @book="createSaleEntry"
      @message="this.messages.push($event)"
      api-path="/api/stock/"
      :reset-on-found="true"
  />
  <DataTable
      :value="sales"
      editMode="cell"
      @cell-edit-complete="onCellEditComplete"
  >
    <Column field="isbn" header="ISBN"></Column>
    <Column field="title" header="Titre"></Column>
    <Column field="quantity" header="Quantité"></Column>
    <Column field="price" header="Prix" key="price">
      <template #editor="{ data, field }">
        <InputText v-model="data[field]" autofocus/>
      </template>
    </Column>
    <Column field="delete">
      <template #body="slotProps">
        <Button icon="pi pi-times" class="p-button-rounded p-button-danger" aria-label="Cancel" @click="deleteRow(slotProps)" />
      </template>
    </Column>

    <Column :row-editor="true" style="width:10%; min-width:8rem" bodyStyle="text-align:center"/>
    <ColumnGroup type="footer">
      <Row>
        <Column footer="Totals:" :colspan="3"/>
        <Column :footer="priceSum()"/>
      </Row>
    </ColumnGroup>
  </DataTable>
</template>

<script>
import IsbnSearch from "@/components/IsbnSearch";


export default {
  name: "SellBook",
  components: {IsbnSearch},
  data() {
    return {
      messages: [],
      sales: []
    }
  },
  methods: {
    deleteRow(sale) {
      const saleIndex = this.sales.indexOf(sale)
      this.sales.splice(saleIndex, 1)
    },
    createSaleEntry(book) {
      this.sales.push({isbn: book.isbn, title: book.title, quantity: 1, price: parseFloat(book.price ?? 0)})
    },
    priceSum() {
      const sum = this.sales.reduce((sum, sale) => sum + sale.price,
          0
      )
      console.log("sum " + sum)
      console.log(sum)
      return sum
    },
    onCellEditComplete(event) {
      let {data, field} = event;
      // TODO find a way to have it done in a proper way
      let newValue = data.undefined;
      if (parseFloat(newValue) >= 0) {
        data[field] = parseFloat(newValue);
      } else {
        event.preventDefault();
      }
    }
  }
}
</script>

<style scoped>

</style>