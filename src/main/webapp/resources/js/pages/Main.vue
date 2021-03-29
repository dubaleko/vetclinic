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
        <h1>Клиники</h1>
        <v-card class="my-2" width="100%" v-for="clinic in clinicPreview" :key="clinic.name">
            <v-card-title>{{clinic.name}}</v-card-title>
            <v-card-text>
                <h3>Адрес: {{clinic.city}}  {{clinic.address}}</h3>
                <h3>Описание: {{clinic.description}}</h3>
            </v-card-text>
        </v-card><br>
        <v-btn color="blue" class="white--text" href="/clinic">Просмотреть всех клиники</v-btn>
        <notifications position="bottom right" group="success" />
    </v-container>
</template>

<script>
    export default {
        name: "Main",
        data(){
            return{
                servicePreview:[],clinicPreview:[]
            }
        },
        methods : {
            getServicePreview(){
                this.$http.get('/api/service/preview').then(function (response) {
                    this.servicePreview = response.body;
                })
            },
            getEmployeePreview(){
                this.$http.get('/api/clinic/preview').then(function (response) {
                    this.clinicPreview = response.body;
                })
            }
        },
        mounted() {
            if (localStorage.getItem("SuccessNotification")) {
                localStorage.clear();
                this.$notify({
                    group: 'success', title: 'Вы записаны',
                    text: 'Вы записаны на прием к врачу. Постарайтесь не опаздывать.', type: 'success'
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