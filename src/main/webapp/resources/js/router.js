import  Vue from 'vue'
import VueRouter from 'vue-router'
import NotFound from "./pages/NotFound.vue";
import Login from "./pages/Login.vue";
import Registration from "./pages/Registration.vue";
import Main from "./pages/Main.vue";
import Service from "./pages/Service.vue";
import Employee from "./pages/Employee.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Main },
    { path: '/login', component: Login },
    { path: '/registration', component: Registration },
    { path: '/service',component: Service},
    { path: '/employee',component: Employee},
    { path: '*', component:NotFound}
]

export default new VueRouter({
    mode: 'history',
    routes
})