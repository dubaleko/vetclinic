<template>
    <v-container>
        <h1>Услуги</h1>
        <v-row v-if="user">
            <service-dialog v-if="user.role == 'ADMIN'" :service="emptyService" :types="serviceType"
                            :typeNames="serviceTypeNames" action="Добавить новую услугу">
            </service-dialog>
        </v-row>
        <v-select v-model="type" :items="myServices" label="Типы услуг"/>
        <table width="100%">
            <tr class="bottom-border" v-for="service in services" :key="service.Name">
                <td align="left">{{service.serviceName}}</td>
                <td align="right">{{service.serviceCost.toFixed(2)}} р.</td>
                <td align="right" v-if="user">
                    <service-dialog v-if="user.role == 'ADMIN'" :service="service" :types="serviceType"
                                    :typeNames="serviceTypeNames" action="Обновить">
                    </service-dialog>
                    <v-btn v-if="user.role == 'ADMIN'" text @click="deleteService(service.id)">Удалить</v-btn>
                </td>
            </tr>
        </table>
        <v-row align="center" justify="center" v-if="services.length < 1">
            Извините но по вашему запросу не найдено никаких услуг
        </v-row>
        <v-pagination v-if="totalPages > 1" @input="getAllService" v-model="page" :length="totalPages" :total-visible="7"
                      prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
    </v-container>
</template>

<script>
    import ServiceDialog from "../components/ServiceDialog.vue";
    export default {
        components: {ServiceDialog},
        props:['user'],
        data(){
            return{
                type : null, page : null, totalPages: null,emptyService : {},
                myServices:[], services:[], serviceType:[], serviceTypeNames:[]
            }
        },
        watch: {
            type: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    this.getAllService(this.page);
                }
            }
        },
        methods :{
            getServiceType(){
                this.$http.get('/api/servicetype').then(function (response) {
                    this.serviceType = response.data;
                    this.serviceType.forEach(element=>{
                        this.serviceTypeNames.push(element.serviceTypeName);
                        this.myServices.push(element.serviceTypeName);
                    })
                    this.myServices.unshift("Все услуги");
                })
            },
            getAllService(page){
                if(!page)
                    page = 1;
                let url = '/api/service?page='+page;
                if (this.type)
                    url += '&type='+this.type;
                this.$http.get(url).then(function (response) {
                    this.services = response.data.content;
                    this.totalPages = response.data.totalPages;
                    this.page = response.data.pageable.pageNumber+1;
                })
            },
            deleteService(id){
                this.$http.delete('/api/service?id='+id).then(function (response) {
                    window.location.href = '/service';
                })
            }
        },
        created() {
            this.getServiceType();
            this.getAllService();
        }
    }
</script>

<style scoped>
        TD { border-bottom: 1px black dashed;}
</style>