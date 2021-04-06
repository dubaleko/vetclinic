<template>
    <v-container>
        <h1>Сотрудники</h1>
        <v-row v-if="user">
            <employee-dialog v-if="user.role == 'MODERATOR' || user.role == 'ADMIN'" :specName="specialization"
                             action="Добавить нового сотрудника" :user="user":clinics="clinics"
                             :clinics-name="clinicsName" :spec="employeeSpec" :employee="emptyEmployee" />
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
           <v-card class="my-2" width="100%" v-for="employee in employees" :key="employee.name">
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
        <v-row align="center" justify="center" v-if="employees.length < 1">
            Извините но по вашему запросу не найдено сотрудников нашей клиники
        </v-row>
    </v-container>
</template>

<script>
    import EmployeeDialog from "../components/EmployeeDialog.vue";
    window.addEventListener('popstate', e=>{
        window.location.reload();
    });
    export default {
        name: "Employee",
        components: {EmployeeDialog},
        props:['user'],
        data(){
            return{
                page : null, totalPages: null, spec: null, emptyEmployee : null,
                employees: [], employeeSpec : [], specialization: [], mySpecs:[],
                clinicsName:[], clinics:[], clinic:null, myClinics:[]
            }
        },
        watch: {
            spec: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    let id = this.getIdByName(this.employeeSpec, this.spec);
                    console.log(this.spec);
                    console.log(id);
                    if (!id){
                        this.spec = "Все специальности";
                        if (this.$route.query.clinic || this.clinic && this.clinic != 'Все клиники'){
                            if (this.$route.query.clinic){
                                window.history.pushState("", "Title", '/employee?clinic='+this.$route.query.clinic);
                            }
                            else {
                                let clinicId = this.getIdByName(this.clinics,this.clinic);
                                window.history.pushState("", "Title", '/employee?clinic='+clinicId);
                            }
                            this.getAllEmployees();
                        }
                        else{
                            window.history.pushState("", "Title", '/employee');
                            this.getAllEmployees();
                        }
                    }
                    else  if (id != this.$route.query.spec){
                        if (this.$route.query.clinic || this.clinic && this.clinic != 'Все клиники'){
                            if (this.$route.query.clinic) {
                                window.history.pushState("", "Title", '/employee?spec=' + id + '&clinic=' + this.$route.query.clinic);
                            }
                            else {
                                let clinicId = this.getIdByName(this.clinics,this.clinic);
                                window.history.pushState("", "Title", '/employee?spec=' + id + '&clinic=' + clinicId);
                            }
                            this.getAllEmployees();
                        }
                        else {
                            window.history.pushState("", "Title", '/employee?spec=' + id);
                            this.getAllEmployees();
                        }
                    }
                }
            },
            clinic: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    let id = this.getIdByName(this.clinics,this.clinic);
                    if (!id){
                        this.clinic = "Все клиники";
                        if (this.$route.query.spec || this.spec && this.spec != 'Все специальности') {
                            if(this.$route.query.spec)  {
                                window.history.pushState("", "Title", '/employee?spec=' + this.$route.query.spec);
                            } else {
                                let specId = this.getIdByName(this.employeeSpec, this.spec);
                                window.history.pushState("", "Title", '/employee?spec=' + specId);
                            }
                            this.getAllEmployees();
                        }
                        else{
                            window.history.pushState("", "Title", '/employee');
                            this.getAllEmployees();
                        }
                    }
                    else if (id != this.$route.query.clinic){
                        if (this.$route.query.spec || this.spec && this.spec != 'Все специальности'){
                            if (this.$route.query.spec) {
                                window.history.pushState("", "Title", '/employee?spec=' + this.$route.query.spec + '&clinic=' + id);
                            }
                            else if (this.spec){
                                let specId = this.getIdByName(this.employeeSpec, this.spec);
                                window.history.pushState("", "Title", '/employee?spec=' + specId + '&clinic=' + id);
                            }
                            this.getAllEmployees();
                        }
                        else {
                            window.history.pushState("", "Title",  '/employee?clinic='+id);
                            this.getAllEmployees();
                        }
                    }
                }
            }
        },
        methods:{
            getAllEmployees(page){
                if(!page)
                    page = 1;
                let url = '/api/employee?page='+page
                if (this.spec && this.spec != 'Все специальности'){
                    let id = this.getIdByName(this.employeeSpec, this.spec);
                    url += '&spec='+id;
                }
                else if(this.$route.query.spec){
                    url += '&spec='+this.$route.query.spec;
                    this.employeeSpec.forEach(spec=>{
                        if (spec.id == this.$route.query.spec){
                            this.spec = spec.name;
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
                    this.employees = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.employees.forEach(element=>{
                        element.url="/order?employee_id="+element.id;
                    })
                })
            },
            deleteEmployee(id){
                this.$http.delete('/api/employee?id='+id).then(function (response) {
                    window.location.href = '/employee';
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