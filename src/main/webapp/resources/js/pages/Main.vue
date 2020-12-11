<template>
    <v-container>
        <h1>Услуги</h1>
        <table width="100%">
            <tr class="bottom-border" v-for="service in servicePreview" :key="service.Name">
                <td align="left">{{service.serviceName}}</td>
                <td align="right">{{service.serviceCost.toFixed(2)}} р.</td>
            </tr>
        </table><br>
        <v-btn color="blue" class="white--text" href="/service">Просмотреть все услуги</v-btn>
        <h1>Сотрудники клиники</h1>
        <v-card class="my-2" width="100%" v-for="employee in employeePreview" :key="employee.name">
            <v-card-title>{{employee.name}}</v-card-title>
            <v-card-text>
                <h3>Должность: {{employee.position}}</h3>
                <h3>Образование: {{employee.education}}</h3>
                <h3>Специализация: <span v-for="object in employee.specs">
                       {{object.specialization}}
                   </span></h3>
            </v-card-text>
        </v-card><br>
        <v-btn class="mr-4" href="/order">Запись на прием</v-btn>
        <v-btn color="blue" class="white--text" href="/employee">Просмотреть всех сотрудников</v-btn>
    </v-container>
</template>

<script>
    export default {
        name: "Main",
        data(){
            return{
                servicePreview:[],employeePreview:[]
            }
        },
        methods : {
            getServicePreview(){
                this.$http.get('/api/service/preview').then(function (response) {
                    this.servicePreview = response.data.content;
                })
            },
            getEmployeePreview(){
                this.$http.get('/api/employee/preview').then(function (response) {
                    this.employeePreview = response.body;
                })
            }
        },
        created() {
            this.getServicePreview();
            this.getEmployeePreview();
        }
    }
</script>

<style scoped>
    TABLE{size: 15px;}
    TD { border-bottom: 1px black dashed;}
</style>