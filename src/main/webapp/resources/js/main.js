import Vue from 'vue'
import VueResource from 'vue-resource'
import Vuetify from 'vuetify'
import Vuelidate from 'vuelidate'
import router from "./router";
import Notifications from 'vue-notification'
import App from "./pages/App.vue";
import 'vuetify/dist/vuetify.min.css'

Vue.use(Vuetify)
Vue.use(Vuelidate)
Vue.use(VueResource)
Vue.use(Notifications)

new Vue({
    el: '#app',
    router,
    vuetify: new Vuetify(),
    render: a => a(App),
})
