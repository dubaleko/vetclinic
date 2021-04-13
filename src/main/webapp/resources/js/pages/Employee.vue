<template>
    <v-container>
        <h1>Сотрудники</h1>
        <v-row v-if="user">
            <employee-dialog v-if="user.role == 'MODERATOR' || user.role == 'ADMIN'" :specName="specialization"
                             action="Добавить нового сотрудника" :user="user":clinics="clinics"
                             :clinics-name="clinicsName" :spec="employeeSpec" />
        </v-row>
        <v-row align="center">
            <v-col >
                <v-select v-model="clinic" :items="myClinics" label="Клиника"/>
            </v-col>
            <v-col>
                <v-select  v-model="spec" :items="mySpecs" label="Тип услуги"/>
            </v-col>
        </v-row>
        <v-row>
           <v-card class="my-2" width="100%" v-for="employee in employees" :key="employee.id">
               <v-card-title>{{employee.name}}</v-card-title>
               <v-card-text>
                   <h3>Клиника: {{employee.clinic.name}}</h3>
                   <h3>Должность: {{employee.position}}</h3>
                   <h3>Образование: {{employee.education}}</h3>
                   <h3>Специализация: <span v-for="object in employee.specs">
                       {{object.name}}
                   </span></h3>
               </v-card-text>
               <v-card-actions>
                   <v-btn color="blue" class="white--text"  :href="employee.url">Запись на прием</v-btn>
                   <div v-if="user">
                       <div v-if="user.role == 'ADMIN' ||
                               user.role == 'MODERATOR' && user.clinic.name == employee.clinic.name">
                           <employee-dialog action="Обновить" :spec="employeeSpec" :user="user" :clinics="clinics"
                                            :clinics-name="clinicsName"  :spec-name="specialization" :employee="employee"/>
                           <v-btn text @click="deleteEmployee(employee.id)">Удалить</v-btn>
                       </div>
                   </div>
               </v-card-actions>
           </v-card>
            <v-pagination v-if="totalPages > 1" @input="getAllEmployees" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </v-row>
        <v-row align="center" justify="center" v-if="emptyEmployees">
            Извините но по вашему запросу не найдено сотрудников клиник партнеров
        </v-row>
    </v-container>
</template>

<script>
    import EmployeeDialog from "../components/EmployeeDialog.vue";
    import {getVariableFromQuery,pushNewState,checkQueryParameter} from "../methods.js";
    window.addEventListener('popstate', e=>{
        window.location.reload();
    });
    export default {
        name: "Employee",
        components: {EmployeeDialog},
        props:['user'],
        data(){
            return{
                page : null, totalPages: null, spec: null, data: null, emptyEmployees : false,
                employees: [], employeeSpec : [], specialization: [], mySpecs:[],
                clinicsName:[], clinics:[], clinic:null, myClinics:[]
            }
        },
        watch: {
            spec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    pushNewState(this.employeeSpec, this.spec, this.clinics, this.clinic,
                        'employee','spec','clinic',);
                    this.getAllEmployees();
                }
            },
            clinic: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    pushNewState(this.clinics, this.clinic,this.employeeSpec, this.spec,
                        'employee','clinic','spec');
                    this.getAllEmployees();
                }
            }
        },
        methods:{
            getAllEmployees(page){
                if(!page)
                    page = 1;
                let url = '/api/employee?page='+page
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
                        element.url="/order?employee_id="+element.id;
                    });
                    this.emptyEmployees = false;
                    if (this.employees.length < 1){
                        this.emptyEmployees = true;
                    }
                })
            },
            deleteEmployee(id){
                this.$http.delete('/api/employee?id='+id).then(function (response) {
                    window.location.href = '/employee';
                })
            }
        },
        created() {
            this.$http.get('/api/clinic/all').then(function (response) {
                this.clinics = response.body;
                this.clinics.forEach(clinic=>{
                    this.clinicsName.push(clinic.name);
                    this.myClinics.push(clinic.name);
                });
                this.myClinics.unshift("Все клиники");
                this.$http.get('/api/specialization').then(function (response) {
                    this.employeeSpec = response.data;
                    this.employeeSpec.forEach(element=>{
                        this.specialization.push(element.name);
                        this.mySpecs.push(element.name);
                    });
                    this.mySpecs.unshift("Все специальности");
                    this.getAllEmployees();
                });
            });
        }
    }
</script>

<style scoped>

</style>