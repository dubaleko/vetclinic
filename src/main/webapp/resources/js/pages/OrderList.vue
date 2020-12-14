<template>
    <v-container v-if="!user" fill-height>
        <authenticated/>
    </v-container>
    <v-container v-else-if="user">
        <h1>Ваши талоны</h1>
        <table width="100%">
            <tr class="bottom-border" v-for="order in orders" :key="order.id">
                <td v-if="user.role == 'ADMIN'" align="left">{{order.user.userName}}</td>
                <td align="left">{{order.employeeDto.name}}</td>
                <td align="left">{{order.dateString}}</td>
                <td>{{order.receptionTimeDto.time}}</td>
                <td align="right">
                    <v-btn text @click="refuseOrder(order.id)">Отменить</v-btn>
                </td>
            </tr>
        </table>
    </v-container>
</template>

<script>
    import Authenticated from "../components/Authenticated.vue";
    export default {
        name: "OrderList",
        components: {Authenticated},
        props:['user'],
        data(){
            return{
                orders : [] ,flag : true,
                page : null, totalPages: null,
            }
        },
        methods : {
            getOrderList(){
                if (!this.page)
                    this.page = 1;
                this.$http.get('/api/order?id='+this.user.id+'&page='+this.page).then(function (response){;
                    this.orders = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;

                    let options = {month: 'long', day: 'numeric', weekday: 'long',};
                    for (let i = 0; i< this.orders.length; i++){
                        let date = new Date(this.orders[i].receptionDateDto.date);
                        this.orders[i].dateString = date.toLocaleString("ru", options);
                    }
                })
            },
            refuseOrder(id){
                this.$http.delete('/api/order?id='+id).then(function (response) {
                    window.location.href = '/order/list';
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
    TD { border-bottom: 1px black dashed;}
</style>