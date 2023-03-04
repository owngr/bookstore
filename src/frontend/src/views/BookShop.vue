<template>
  <h1>Shop</h1>
  <div class="card">
    <DataView :value="books" layout="grid">
      <template #grid="slotProps">
        <div class="col-12 md:col-4">
          <div class="product-grid-item card">
            <div class="product-grid-item-content">
              <img :src="'/api/shop/' + slotProps.data.isbn" :alt="slotProps.data.title"/>
              <div class="product-name">{{slotProps.data.title}}</div>
            </div>
            <div class="product-grid-item-bottom">
              <span class="product-price">{{slotProps.data.authors}}</span>
            </div>
            <div class="product-grid-item-bottom">
              <span class="product-price">{{slotProps.data.price}} CHF</span>
            </div>
          </div>
        </div>
      </template>
    </DataView>

  </div>
</template>

<script>
import ShopService from "@/service/ShopService";
export default {
  name: "BookShop",
  data() {
    return {
      books: []
    }
  },

  mounted() {
    this.fetchStock();
  },
  methods: {
    fetchStock: function () {
      ShopService.getShop().then(
          data => {
            this.books = data
          }
      )
    },
    getPicture: async function (isbn) {
      return await ShopService.getPicture(isbn)
    }
  }
}
</script>

<style scoped>
.card {
  background: #ffffff;
  padding: 2rem;
  box-shadow: 0 2px 1px -1px rgba(0,0,0,.2), 0 1px 1px 0 rgba(0,0,0,.14), 0 1px 3px 0 rgba(0,0,0,.12);
  border-radius: 4px;
  margin-bottom: 2rem;
}
.p-dropdown {
  width: 14rem;
  font-weight: normal;
}

.product-name {
  font-size: 1.5rem;
  font-weight: 700;
}

.product-description {
  margin: 0 0 1rem 0;
}

.product-category-icon {
  vertical-align: middle;
  margin-right: .5rem;
}

.product-category {
  font-weight: 600;
  vertical-align: middle;
}

::v-deep(.product-list-item) {
  display: flex;
  align-items: center;
  padding: 1rem;
  width: 100%;

  img {
    width: 50px;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
    margin-right: 2rem;
  }

  .product-list-detail {
    flex: 1 1 0;
  }

  .p-rating {
    margin: 0 0 .5rem 0;
  }

  .product-price {
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: .5rem;
    align-self: flex-end;
  }

  .product-list-action {
    display: flex;
    flex-direction: PColumn;
  }

  .p-button {
    margin-bottom: .5rem;
  }
}

::v-deep(.product-grid-item) {
  margin: .5rem;
  border: 1px solid var(--surface-border);

  .product-grid-item-top,
  .product-grid-item-bottom {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  img {
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
    margin: 2rem 0;
  }

  .product-grid-item-content {
    text-align: center;
  }

  .product-price {
    font-size: 1.5rem;
    font-weight: 600;
  }
}

</style>