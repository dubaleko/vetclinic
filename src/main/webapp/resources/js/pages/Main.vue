<template>
    <v-container>
        <h1>Услуги</h1>
        <table width="100%">
            <tr class="bottom-border" v-for="service in servicePreview" :key="service.Name">
                <td align="left">{{service.serviceName}}</td>
                <td align="right">{{service.serviceCost.toFixed(2)}} р.</td>
            </tr>
        </table><br>
        <v-btn color="yellow" href="/service">Просмотреть все услуги</v-btn>
    </v-container>
</template>

<script>
    export default {
        name: "Main",
        data(){
            return{
                servicePreview:[]
            }
        },
        methods : {
            getServicePreview(){
                this.$http.get('/api/service/preview').then(function (response) {
                    this.servicePreview = response.data.content;
                })
            }
        },
        created() {
            this.getServicePreview();
        }
    }
</script>

<style scoped>
    TABLE{size: 15px;}
    TD { border-bottom: 1px black dashed;}
</style>