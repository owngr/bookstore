import { createWebHashHistory, createRouter } from 'vue-router'
import Home from "@/views/HomePage.vue"
import AddBook from "@/views/AddBook.vue"
import BookStock from "@/views/BookStock.vue"
import StockInventory from "@/views/StockInventory.vue"
import BookShop from "@/views/BookShop.vue"
import SellBook from "@/views/SellBook.vue"
import Sales from "@/views/BookSales.vue"
import BasketPage from "@/views/BasketPage.vue";
import TagPage from "@/views/TagPage.vue";

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
    },
    {
        path: "/stock",
        name: "Stock",
        component: BookStock,
    },
    {
        path: "/book/add",
        name: "BookAdd",
        component: AddBook,
    },
    {
        path: "/inventory",
        name: "Inventory",
        component: StockInventory,
    },
    {
        path: "/shop",
        name: "Shop",
        component: BookShop,
    },
    {
        path: "/book/sell",
        name: "SellBook",
        component: SellBook,
    },
    {
        path: "/sales",
        name: "sales",
        component: Sales,
    },
    {
        path: "/basket",
        names: "basket",
        component: BasketPage,
    },
    {
        path: "/tag",
        name: "tags",
        component: TagPage,
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router