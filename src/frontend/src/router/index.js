import { createWebHistory, createRouter } from "vue-router";
import Home from "@/views/Home.vue";
import AddBook from "@/views/AddBook";

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
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