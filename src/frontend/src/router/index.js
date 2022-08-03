import { createWebHashHistory, createRouter } from "vue-router"
import Home from "@/views/Home.vue"
import AddBook from "@/views/AddBook"
import Stock from "@/views/Stock"
import Inventory from "@/views/Inventory"
import Shop from "@/views/Shop"
import SellBook from "@/views/SellBook"

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
    },
    {
        path: "/stock",
        name: "Stock",
        component: Stock,
    },
    {
        path: "/book/add",
        name: "BookAdd",
        component: AddBook,
    },
    {
        path: "/inventory",
        name: "Inventory",
        component: Inventory,
    },
    {
        path: "/shop",
        name: "Shop",
        component: Shop,
    },
    {
        path: "/book/sell",
        name: "SellBook",
        component: SellBook,
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router