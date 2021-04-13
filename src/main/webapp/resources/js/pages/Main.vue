<template>
    <v-container>
        <h1>Услуги</h1>
        <table v-for="service in servicePreview" :key="service.id">
            <tr class="bottom-border">
                <td align="left">{{service.serviceName}}</td>
                <td align="left">{{service.clinic.name}}</td>
                <td align="right">{{service.serviceCost.toFixed(2)}} р.</td>
            </tr>
        </table>
        <div class="mb-2"/>
        <v-btn  color="blue" class="white--text" href="/service">Просмотреть все услуги</v-btn>
        <h1>Клиники</h1>
        <v-card class="my-2" width="100%" v-for="clinic in clinicPreview" :key="clinic.id">
            <v-card-title>{{clinic.name}}</v-card-title>
            <v-card-text>
                <h3>Адрес: {{clinic.city}}  {{clinic.address}}</h3>
                <h3>Описание: {{clinic.description}}</h3>
            </v-card-text>
        </v-card>
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
            this.$http.get('/api/service/preview').then(function (response) {
                this.servicePreview = response.body;
            });
            this.$http.get('/api/clinic/preview').then(function (response) {
                this.clinicPreview = response.body;
            });
        }
    }
</script>

<style scoped>
    v-btn {margin-top: 20%;}
    table {width: 100%;}
    TD { border-bottom: 1px black dashed;}
    TD:first-child{width: 70%}
    TD:nth-child{width: 15%}
</style>