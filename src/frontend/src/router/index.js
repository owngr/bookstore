import { createWebHashHistory, createRouter } from "vue-router"
import Home from "@/views/HomePage.vue"
import AddBook from "@/views/AddBook"
import BookStock from "@/views/BookStock"
import StockInventory from "@/views/StockInventory"
import BookShop from "@/views/BookShop"
import SellBook from "@/views/SellBook"
import Sales from "@/views/BookSales"
import BasketPage from "@/views/BasketPage";

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
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router