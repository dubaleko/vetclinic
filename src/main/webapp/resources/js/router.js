import  Vue from 'vue'
import VueRouter from 'vue-router'
import NotFound from "./pages/NotFound.vue";
import Main from "./pages/Main.vue";
import Service from "./pages/Service.vue";
import Employee from "./pages/Employee.vue";
import OrderList from "./pages/OrderList.vue";
import Users from "./pages/Users.vue";
import Order from "./pages/Order.vue";
import Clinic from "./pages/Clinic.vue";
import Me from "./pages/Me.vue";
import VisitLog from "./pages/VisitLog.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Main },
    {path: '/clinic', component: Clinic},
    { path: '/service',component: Service},
    { path: '/employee',component: Employee},
    {path: '/order',component:Order},
    {path: '/talons',component:OrderList},
    {path: '/users', component: Users},
    {path: '/me',component: Me},
    {path: '/visitLog', component: VisitLog},
    { path: '*', component:NotFound}
]

export default new VueRouter({
    mode: 'history',
    routes
})
