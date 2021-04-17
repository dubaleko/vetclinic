<template>
    <v-container v-if="!user" fill-height>
        <authenticated/>
    </v-container>
    <v-container v-else-if="user">
        <h1>Запись на прием</h1>
        <div v-if="dateId">
            <v-row justify="center">
                <notifications position="bottom right" group="error"/>
                <v-col v-for="time in receptionTime" :key="time.id">
                    <v-card :elevation="1" >
                        <v-card-title>
                            <v-btn v-if="time.occupied" disabled  text>{{time.time}}</v-btn>
                            <v-btn @click="registrationOrder(time)" v-else-if="!time.occupied" text>
                                {{time.time}}</v-btn>
                        </v-card-title>
                    </v-card>
                </v-col>
            </v-row>
            <v-row  align="center" justify="center" v-if="emptyTimes">
                Извините но мы не смогли найти запрашиваемые вами данные
            </v-row>
        </div>
        <div v-else-if ="employeeId">
            <v-row justify="center">
                <v-col v-for="date in receptionDate" :key="date.id">
                    <v-card  :elevation="1" >
                        <v-card-title>
                            <v-btn :href="date.url" text>{{date.dateString}}</v-btn>
                        </v-card-title>
                    </v-card>
                </v-col>
            </v-row>
            <v-row  align="center" justify="center" v-if="emptyDates">
                Извините но на данный момент у этого врача нельзя записаться на прием онлайн
            </v-row>
        </div>
        <div v-else>
            <v-row align="center">
                <v-col >
                    <v-select v-model="clinic" :items="clinicsName" label="Клиника"/>
                </v-col>
                <v-col>
                    <v-select  v-model="spec" :items="specialization" label="Тип услуги"/>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col v-for="employee in employees" :key="employee.id">
                    <v-card  :elevation="1" >
                        <v-card-title>
                            <v-btn text :href="employee.url">{{employee.name}}</v-btn>
                        </v-card-title>
                        <v-card-text>
                            <h4>Клиника {{employee.clinic.name}}</h4>
                            <h4>
                                <span v-for="object in employee.specs">
                                    {{object.name}}
                                </span>
                            </h4>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
            <v-row align="center" justify="center" v-if="emptyEmpls">
                Извините но по вашему запросу не найдено сотрудников клиник партнеров
            </v-row>
            <v-pagination v-if="totalPages > 1" @input="getAllEmployees" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </div>
    </v-container>
</template>

<script>
    import Authenticated from "../components/Authenticated.vue";
    import {getVariableFromQuery,pushNewState,checkQueryParameter} from "../methods.js";
    export default {
        name: "Order",
        components: {Authenticated},
        props:['user'],
        data(){
            return{
                page: null, totalPages: null, flag: true, employeeId: null, dateId : null,
                spec: null, clinic:null, value: null, emptyEmpls: false, emptyDates: false,
                emptyTimes: false, employees: [], employeeSpec: [], specialization:[],
                receptionDate:[], receptionTime:[], clinics:[], clinicsName:[]
            }
        },
        watch: {
            clinic: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    pushNewState(this.clinics, this.clinic,this.employeeSpec, this.spec,
                        'order','clinic','spec');
                    this.getAllEmployees();
                }
            },
            spec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    pushNewState(this.employeeSpec, this.spec, this.clinics, this.clinic,
                        'order','spec','clinic',);
                    this.getAllEmployees();
                }
            }
        },
        methods : {
            getAllEmployees(page){
                if(!page)
                    page = 1;
                let url = '/api/employee?page='+page+'&size=12';
                url += checkQueryParameter(this.employeeSpec,this.spec,this.$route.query.spec,
                    'spec','Все специальности');
                if (!this.spec && this.$route.query.spec){
                    this.spec = getVariableFromQuery(this.employeeSpec, this.$route.query.spec);
                }
                url += checkQueryParameter(this.clinics,this.clinic,this.$route.query.clinic,
                    'clinic','Все клиники');
                if (!this.clinic && this.$route.query.clinic) {
                    this.clinic = getVariableFromQuery(this.clinics, this.$route.query.clinic)
                }
                this.$http.get(url).then(function (response) {
                    this.employees = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.employees.forEach(element=>{
                        element.url="/order?employee="+element.id;
                    })
                    this.emptyEmpls = false;
                    if (this.employees.length < 1){
                        this.emptyEmpls = true;
                    }
                })
            },
            registrationOrder(time){
                let order = {
                    employee: time.receptionDateDto.employeeDto,
                    receptionDate: time.receptionDateDto,
                    receptionTime: time,
                    user: this.user
                }
                this.$http.post('/api/order', order).then(function (response) {
                    localStorage.setItem("SuccessNotification", "Yes");
                    window.location.href = '/';
                }, (response) => {
                    this.$notify({group: 'error', title: 'Ошибка при записи на прием',type: 'error',
                        text: 'Вы не можете произвести запись на данное место так как оно уже занято.'
                    })
                })
            }
        },
        created(){
            if (this.$route.query.date){
                this.dateId = this.$route.query.date;
                this.$http.get('api/reception/time/'+this.dateId).then(function (response) {
                    this.receptionTime = response.body;
                    this.emptyTimes = false;
                    if (this.receptionTime.length < 1){
                        this.emptyTimes = true;
                    }
                })
            }
            else if (this.$route.query.employee){
                this.employeeId = this.$route.query.employee;
                this.$http.get('api/reception/date/'+this.employeeId).then(function (response) {
                    this.receptionDate = response.body;
                    this.emptyDates = false;
                    if (this.receptionDate.length < 1){
                        this.emptyDates = true;
                    }
                    let options = {month: 'long', day: 'numeric', weekday: 'long',};
                    for (let i = 0; i< this.receptionDate.length; i++){
                        let date = new Date(this.receptionDate[i].date);
                        this.receptionDate[i].url = "/order?date="+this.receptionDate[i].id;
                        this.receptionDate[i].dateString = date.toLocaleString("ru", options);
                    }
                })
            }
            else {
                this.$http.get('/api/clinic/all').then(function (response) {
                    this.clinics = response.body;
                    this.clinics.forEach(clinic=>{
                        this.clinicsName.push(clinic.name);
                    });
                    this.clinicsName.unshift("Все клиники");
                    this.$http.get('/api/specialization').then(function (response) {
                        this.employeeSpec = response.data;
                        this.employeeSpec.forEach(element=>{
                            this.specialization.push(element.name);
                        });
                        this.specialization.unshift("Все специальности");
                        this.getAllEmployees();
                    });
                });
            }
        }
    }
</script>

<style scoped>
</style>