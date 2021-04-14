<template>
    <v-container v-if="!user" fill-height>
        <authenticated/>
    </v-container>
    <v-container v-else-if="user">
        <h1>Талоны</h1>
        <v-expansion-panels>
            <v-expansion-panel class="mb-4" v-for="order in orders" :key="order.id">
                <v-expansion-panel-header>
                <div v-if="user.role != 'USER'">{{order.user.userName}}</div>
                <div v-if="user.role != 'DOCTOR'">{{order.employeeDto.name}}</div>
                {{order.receptionTimeDto.time}}<v-spacer/>
                {{order.dateString}}
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                    <card  v-if="user.role != 'USER'" :order="order" :user="order.user"/>
                    <v-btn text @click="refuseOrder(order.id)">Отменить</v-btn>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-expansion-panels>
        <v-row justify="center" v-if="emptyOrders">
            Извините но мы не смогли найти талонов на прием к врачу
        </v-row>
        <v-pagination v-if="totalPages > 1" @input="getOrderList" v-model="page" :length="totalPages" :total-visible="7"
                      prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
    </v-container>
</template>

<script>
    import Authenticated from "../components/Authenticated.vue";
    import Card from "../components/Card.vue";
    export default {
        name: "OrderList",
        components: {Card, Authenticated},
        props:['user'],
        data(){
            return{
                orders : [] ,flag : true, emptyOrders: false,
                page : null, totalPages: null,
            }
        },
        methods : {
            getOrderList(){
                let options = {month: 'long', day: 'numeric', weekday: 'long'};
                if (!this.page)
                    this.page = 1;
                this.$http.get('/api/order?id='+this.user.id+'&page='+this.page).then(function (response){;
                    this.orders = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.emptyOrders = false;
                    if (this.orders.length < 1){
                        this.emptyOrders = true;
                    }
                    this.orders.forEach(order=>{
                        let date = new Date(order.receptionDateDto.date);
                        order.dateString = date.toLocaleDateString("ru",options);
                    })
                })
            },
            refuseOrder(id){
                this.$http.delete('/api/order?id='+id).then(function (response) {
                    window.location.href = '/talons';
                })
            }
        },
        updated() {
            if (this.user && this.flag) {
                this.flag = false;
                this.getOrderList();
            }
        }
    }
</script>

<style scoped>
</style>