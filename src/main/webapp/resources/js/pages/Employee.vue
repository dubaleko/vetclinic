<template>
    <v-container>
        <h1>Наши Сотрудники</h1>
        <v-row v-if="user">
            <employee-dialog  v-if="user.role == 'ADMIN'" action="Добавить нового сотрудника"
                              :specName="specialization" :spec="employeeSpec" :employee="emptyEmployee" />
        </v-row>
        <v-select v-model="searchSpec" :items="myEmployeeSpec" label="Специализация"/>
        <v-row>
           <v-card class="my-2" width="100%" v-for="employee in employees" :key="employee.name">
               <v-card-title>{{employee.name}}</v-card-title>
               <v-card-text>
                   <h3>Должность: {{employee.position}}</h3>
                   <h3>Образование: {{employee.education}}</h3>
                   <h3>Специализация: <span v-for="object in employee.specs">
                       {{object.specialization}}
                   </span></h3>
               </v-card-text>
               <v-card-actions>
                   <v-btn color="blue" class="white--text"  :href="employee.url">Запись на прием</v-btn>
                   <div v-if="user">
                       <employee-dialog v-if="user.role == 'ADMIN'" action="Обновить" :spec="employeeSpec"
                       :spec-name="specialization" :employee="employee"/>
                       <v-btn v-if="user.role == 'ADMIN'" text @click="deleteEmployee(employee.id)">Удалить</v-btn>
                   </div>
               </v-card-actions>
           </v-card>
            <v-pagination v-if="totalPages > 1" @input="getAllEmployees" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </v-row>
        <v-row align="center" justify="center" v-if="employees.length < 1">
            Извините но по вашему запросу не найдено сотрудников нашей клиники
        </v-row>
    </v-container>
</template>

<script>
    import EmployeeDialog from "../components/EmployeeDialog.vue";
    export default {
        name: "Employee",
        components: {EmployeeDialog},
        props:['user'],
        data(){
            return{
                page : null, totalPages: null, searchSpec: '', emptyEmployee : {},
                employees: [], employeeSpec : [], myEmployeeSpec: [], specialization: []
            }
        },
        watch: {
            searchSpec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    this.getAllEmployees(this.page);
                }
            }
        },
        methods:{
            getEmployeeSpec(){
                this.$http.get('/api/specialization').then(function (response) {
                    this.employeeSpec = response.data;
                    this.employeeSpec.forEach(element=>{
                        this.specialization.push(element.specialization);
                        this.myEmployeeSpec.push(element.specialization);
                    })
                    this.myEmployeeSpec.unshift("Любая специализация");
                })
            },
            getAllEmployees(page){
                if(!page)
                    page = 1;
                let url = '/api/employee?page='+page;
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
            deleteEmployee(id){
                this.$http.delete('/api/employee?id='+id).then(function (response) {
                    window.location.href = '/employee';
                })
            }
        },
        created() {
            this.getEmployeeSpec();
            this.getAllEmployees();
        }
    }
</script>

<style scoped>

</style>