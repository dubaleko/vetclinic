import  Vue from 'vue'
import VueRouter from 'vue-router'
import NotFound from "./pages/NotFound.vue";
import Main from "./pages/Main.vue";
import Service from "./pages/Service.vue";
import Employee from "./pages/Employee.vue";
import Order from "./pages/Order.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Main },
    { path: '/service',component: Service},
    { path: '/employee',component: Employee},
    {path: '/order',component:Order},
    { path: '*', component:NotFound}
]

export default new VueRouter({
    mode: 'history',
    routes
})