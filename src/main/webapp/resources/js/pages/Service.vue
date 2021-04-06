<template xmlns="http://www.w3.org/1999/html">
    <v-container>
        <h1>Услуги</h1>
        <v-row v-if="user">
            <service-dialog v-if="user.role == 'MODERATOR' || user.role == 'ADMIN'" :service="emptyService" :user="user"
                            :types="serviceType" :clinics="clinics" :clinics-name="clinicsName"
                            :typeNames="serviceTypeNames" action="Добавить новую услугу">
            </service-dialog>
        </v-row>
        <v-row align="center">
            <v-col >
                <v-select v-model="clinic" :items="myClinics" label="Клиника"/>
            </v-col>
            <v-col>
            <v-select  v-model="type" :items="myServices" label="Тип услуги"/>
            </v-col>
        </v-row>
        <table v-for="service in services" :key="service.Name">
            <tr class="bottom-border">
                <td align="left">{{service.serviceName}}</td>
                <td align="left">{{service.clinic.name}}</td>
                <td align="right">{{service.serviceCost.toFixed(2)}} р.</td>
            </tr>
            <div v-if="user">
                    <div v-if="user.role == 'ADMIN' ||
                               user.role == 'MODERATOR' && user.clinic.name == service.clinic.name">
                        <service-dialog :service="service" :types="serviceType" :user="user" :clinics="clinics"
                                        :clinics-name="clinicsName" :typeNames="serviceTypeNames" action="Обновить">
                        </service-dialog>
                        <v-btn text @click="deleteService(service.id)">Удалить</v-btn>
                    </div>
            </div>
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
    window.addEventListener('popstate', e=>{
        window.location.reload();
    });
    export default {
        components: {ServiceDialog},
        props:['user'],
        data(){
            return{
                type : null, page : null, totalPages: null, emptyService : null,
                services:[], serviceType:[], serviceTypeNames:[], myServices:[],
                clinicsName:[], clinics:[], clinic:null, myClinics:[]
            }
        },
        watch: {
            type: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    let id = this.getIdByName(this.serviceType, this.type);
                    if (!id){
                        this.type = "Все услуги";
                        if (this.$route.query.clinic || this.clinic && this.clinic != 'Все клиники'){
                            if (this.$route.query.clinic){
                                window.history.pushState("", "Title", '/service?clinic='+this.$route.query.clinic);
                            }
                            else {
                                let clinicId = this.getIdByName(this.clinics,this.clinic);
                                window.history.pushState("", "Title", '/service?clinic='+clinicId);
                            }
                            this.getAllService();
                        }
                        else{
                            window.history.pushState("", "Title", '/service');
                            this.getAllService();
                        }
                    }
                    else  if (id != this.$route.query.serviceType){
                        if (this.$route.query.clinic || this.clinic && this.clinic != 'Все клиники'){
                            if (this.$route.query.clinic) {
                                window.history.pushState("", "Title", '/service?serviceType=' + id + '&clinic=' + this.$route.query.clinic);
                            }
                            else {
                                let clinicId = this.getIdByName(this.clinics,this.clinic);
                                window.history.pushState("", "Title", '/service?serviceType=' + id + '&clinic=' + clinicId);
                            }
                            this.getAllService();
                        }
                        else {
                            window.history.pushState("", "Title", '/service?serviceType=' + id);
                            this.getAllService();
                        }
                    }
                }
            },
            clinic: function (newTemplate, oldTemplate) {
              if (newTemplate != oldTemplate){
                  let id = this.getIdByName(this.clinics,this.clinic);
                  if (!id){
                      this.clinic = "Все клиники";
                      if (this.$route.query.serviceType || this.type && this.type != 'Все услуги' ){
                          if (this.$route.query.serviceType ) {
                              window.history.pushState("", "Title", '/service?serviceType=' + this.$route.query.serviceType);
                          }
                          else {
                              let serviceId = this.getIdByName(this.serviceType, this.type);;
                              window.history.pushState("", "Title", '/service?serviceType='+serviceId);
                          }
                          this.getAllService();
                      }
                      else{
                          window.history.pushState("", "Title", '/service');
                          this.getAllService();
                      }
                  }
                  else if (id != this.$route.query.clinic){
                      if (this.$route.query.serviceType || this.type && this.type != 'Все услуги'){
                          if (this.$route.query.serviceType) {
                              window.history.pushState("", "Title", '/service?serviceType=' + this.$route.query.serviceType + '&clinic=' + id);
                          }
                          else if (this.type){
                              let serviceId = this.getIdByName(this.serviceType, this.type);;
                              window.history.pushState("", "Title", '/service?serviceType=' + serviceId + '&clinic=' + id);
                          }
                          this.getAllService();
                      }
                      else {
                          window.history.pushState("", "Title",  '/service?clinic='+id);
                          this.getAllService();
                      }
                  }
              }
            }
        },
        methods :{
            getAllService(page){
                if(!page)
                    page = 1;
                let url = '/api/service?page='+page
                if (this.type && this.type != 'Все услуги'){
                    let id = this.getIdByName(this.serviceType, this.type);
                    url += '&serviceType='+id;
                }
                else if(this.$route.query.serviceType){
                    url += '&serviceType='+this.$route.query.serviceType;
                    this.serviceType.forEach(serviceType=>{
                        if (serviceType.id == this.$route.query.serviceType){
                            this.type = serviceType.name;
                        }
                    })
                }
                if (this.clinic && this.clinic != 'Все клиники'){
                    let id = this.getIdByName(this.clinics,this.clinic);
                    url+='&clinic='+id;
                }
                else if (this.$route.query.clinic){
                    url+='&clinic='+this.$route.query.clinic;
                    this.clinics.forEach(clinic=>{
                        if (clinic.id == this.$route.query.clinic){
                            this.clinic = clinic.name;
                        }
                    })
                }
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
            },
            getIdByName(elements,name){
                let id;
                elements.forEach(element=>{
                    if (name == element.name){
                        id = element.id;
                    }
                });
                return id;
            }
        },
        created() {
            this.$http.get('/api/clinic/all').then(function (response) {
                this.clinics = response.body;
                this.clinics.forEach(clinic=>{
                    this.clinicsName.push(clinic.name);
                    this.myClinics.push(clinic.name);
                })
                this.myClinics.unshift("Все клиники");
                this.$http.get('/api/servicetype').then(function (response) {
                    this.serviceType = response.data;
                    this.serviceType.forEach(element=>{
                        this.serviceTypeNames.push(element.name);
                        this.myServices.push(element.name);
                    })
                    this.myServices.unshift("Все услуги");
                    this.getAllService();
                });
            });
        }
    }
</script>

<style scoped>
    table {width: 100%;}
    TD { border-bottom: 1px black dashed;}
    TD:first-child{width: 70%}
    TD:nth-child{width: 15%}
</style>