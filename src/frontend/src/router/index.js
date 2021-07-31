import { createWebHistory, createRouter } from "vue-router";
import Home from "@/views/Home.vue";
import AddBook from "@/views/AddBook";
import Stock from "@/views/Stock";

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
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;