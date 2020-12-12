<template>
    <v-container v-if="!user" fill-height>
        <div class="centerDiv">
            <h4>Эта страница доступна только зарегистрированным пользователям</h4>
            <div class="littleDiv">
                <login-dialog/>
                <registration-dialog/>
            </div>
        </div>
    </v-container>
    <v-container v-else-if="user">
        <h1>Запись на прием</h1>
        <div v-if="!userId">
            <v-select v-model="searchSpec" :items="specialization" label="Специализация"/>
            <v-row justify="center">
                <v-col v-for="employee in employees" :key="employee.name" cols="4" >
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
                Извините но по вашему запросу не найдено сотрудников нашей клиники
            </v-row>
            <v-pagination v-if="totalPages > 1" @input="getAllEmployees" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </div>
        <div v-if="userId">
            Выберите дату и время
        </div>
    </v-container>
</template>

<script>
    import LoginDialog from "../components/LoginDialog.vue";
    import RegistrationDialog from "../components/RegistrationDialog.vue";
    export default {
        name: "Order",
        components: {LoginDialog,RegistrationDialog},
        props:['user'],
        data(){
            return{
                page : null, totalPages: null, searchSpec: '', userId: null,
                employees: [], employeeSpec : [], specialization: [], flag : true
            }
        },
        watch: {
            searchSpec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
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
                let url = '/api/employee?page='+page+'&size='+18;
                if (this.searchSpec)
                    url += '&spec='+this.searchSpec;
                this.$http.get(url).then(function (response) {
                    this.employees = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.employees.forEach(element=>{
                        element.url="/order?employee="+element.id;
                    })
                })
            },
        },
        updated() {
            if (this.user && this.flag) {
                this.flag = false;
                if (this.$route.query.employee){
                    this.userId = this.$route.query.employee;
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
    .centerDiv{
        position: absolute;
        top: 40%;
        left: 50%;
        margin-right: -45%;
        transform: translate(-50%, -50%)
    }
    .littleDiv{
        position: absolute;
        top: 200%;
        left: 25%;
    }
</style>