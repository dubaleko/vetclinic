<template>
    <v-container v-if="!user" fill-height>
        <authenticated/>
    </v-container>
    <v-container v-else-if="user">
        <h1>Запись на прием</h1>
        <div v-if="!userId">
            <v-select v-model="searchSpec" :items="specialization" label="Специализация"/>
            <v-row justify="center">
                <v-col v-for="employee in employees" :key="employee.name">
                    <v-card  :elevation="1" >
                        <v-card-title>
                            <v-btn text :href="employee.url">{{employee.name}}</v-btn>
                        </v-card-title>
                        <v-card-text>
                            <h4>
                                <span v-for="object in employee.specs">
                                    {{object.specialization}}
                                </span>
                            </h4>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
            <v-row align="center" justify="center" v-if="employees.length < 1">
                Извините но по вашему запросу не найдено сотрудников клиник партнеров
            </v-row>
            <v-pagination v-if="totalPages > 1" @input="getAllEmployees" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </div>
        <div v-else-if="userId && !dateId">
            <v-row justify="center">
                <v-col v-for="date in receptionDate" :key="date.date">
                    <v-card  :elevation="1" >
                        <v-card-title>
                            <v-btn :href="date.url" text>{{date.dateString}}</v-btn>
                        </v-card-title>
                    </v-card>
                </v-col>
            </v-row>
            <v-row  align="center" justify="center" v-if="receptionDate.length < 1">
                Извините но на данный момент у этого врача нельзя записаться на прием онлайн
            </v-row>
        </div>
        <div v-else-if="dateId">
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
            <v-row  align="center" justify="center" v-if="receptionTime.length < 1">
                Извините но мы не смогли найти запрашиваемые вами данные
            </v-row>
        </div>
    </v-container>
</template>

<script>
    import Authenticated from "../components/Authenticated.vue";
    export default {
        name: "Order",
        components: {Authenticated},
        props:['user'],
        data(){
            return{
                page: null, totalPages: null, searchSpec: '',
                flag: true, userId: null, dateId : null,
                employees: [], employeeSpec: [], specialization:[],
                receptionDate:[], receptionTime:[],
            }
        },
        watch: {
            searchSpec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    this.page = 1;
                    this.getAllEmployees(this.page);
                }
            }
        },
        methods : {
            getEmployeeSpec(){
                this.$http.get('/api/specialization').then(function (response) {
                    this.employeeSpec = response.data;
                    this.employeeSpec.forEach(element=>{
                        this.specialization.push(element.specialization);
                    })
                    this.specialization.unshift("Любая специализация");
                })
            },
            getAllEmployees(page){
                if(!page)
                    page = 1;
                let url = '/api/employee?page='+page+'&size='+18+'&spec='+this.searchSpec;
                this.$http.get(url).then(function (response) {
                    this.employees = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.employees.forEach(element=>{
                        element.url="/order?employee_id="+element.id;
                    })
                })
            },
            getReceptionDate(){
                this.$http.get('api/reception/date/'+this.userId).then(function (response) {
                    this.receptionDate = response.body;
                    let options = {month: 'long', day: 'numeric', weekday: 'long',};
                    for (let i = 0; i< this.receptionDate.length; i++){
                        let date = new Date(this.receptionDate[i].date);
                        this.receptionDate[i].url = "/order?employee_id="+this.userId+
                            "&date_id="+this.receptionDate[i].id;
                        this.receptionDate[i].dateString = date.toLocaleString("ru", options);
                    }
                })
            },
            getReceptionTime(){
                this.$http.get('api/reception/time/'+this.dateId).then(function (response) {
                    this.receptionTime = response.body;
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
        updated() {
            if (this.user && this.flag) {
                this.flag = false;
                if (this.$route.query.employee_id){
                    this.userId = this.$route.query.employee_id;
                    if (this.$route.query.date_id){
                        this.dateId = this.$route.query.date_id;
                        this.getReceptionTime();
                    }
                    else {
                        this.getReceptionDate();
                    }
                }
                else {
                    this.getEmployeeSpec();
                    this.getAllEmployees();
                }
            }
        }
    }
</script>

<style scoped>
</style>