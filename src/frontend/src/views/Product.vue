<template>
  <div>
    <DataTable :value="products" responsiveLayout="scroll">
      <Column field="code" header="Code"></Column>
      <Column field="name" header="Name"></Column>
      <Column field="category" header="Category"></Column>
      <Column field="quantity" header="Quantity"></Column>
    </DataTable>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import ProductService from '../service/ProductService';

export default {
  setup() {
    onMounted(() => {
      productService.value.getProductsSmall().then(data => products.value = data);
    })

    const products = ref();
    const productService = ref(new ProductService());

    return { products, productService }
  }
}
</script>