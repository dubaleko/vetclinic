<template>
    <v-container>
        <h1>Клиники</h1>
        <v-row v-if="user && user.role == 'ADMIN'">
            <clinic-dialog :clinic="emptyClinic" action="Добавить новую клинику"/>
        </v-row>
        <v-select v-model="searchCity" :items="cities" label="Город в котором расположена клиника"/>
        <v-row>
            <v-card class="my-2" width="100%" v-for="clinic in clinics" :key="clinic.name">
                <v-card-title>{{clinic.name}}</v-card-title>
                <v-card-text>
                    <h3>Адрес: {{clinic.city}}  {{clinic.address}}</h3>
                    <h3>Описание: {{clinic.description}}</h3>
                </v-card-text>
                <v-card-actions>
                    <v-btn color="blue" class="white--text">Услуги</v-btn>
                    <v-btn color="blue" class="white--text" >Сотрудники</v-btn>
                    <div v-if="user">
                        <div v-if="user.role == 'ADMIN' ||
                                        user.role == 'MODERATOR' && user.clinic.name == clinic.name">
                            <clinic-dialog :clinic="clinic" action="Обновить"/>
                            <v-btn text @click="deleteClinic(clinic.id)">Удалить</v-btn>
                        </div>
                    </div>
                </v-card-actions>
            </v-card>
            <v-pagination v-if="totalPages > 1" @input="getAllClinics" v-model="page" :length="totalPages" :total-visible="7"
                          prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </v-row>
        <v-row align="center" justify="center" v-if="clinics.length< 1">
            Извините но по вашему запросу не найдено ветеринарных клиник
        </v-row>
    </v-container>
</template>

<script>
    import ClinicDialog from "../components/ClinicDialog.vue";
    export default {
        name: "Clinic",
        components: {ClinicDialog},
        props:['user'],
        data(){
            return{
                page : null, totalPages: null, searchCity: '',
                emptyClinic : {}, clinics : [], cities: []
            }
        },
        watch: {
            searchCity: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    this.page = 1;
                    this.getAllClinics(this.page);
                }
            }
        },
        methods: {
            getAllClinics(page){
                if(!page)
                    page = 1;
                let url = '/api/clinic?page='+page +'&city='+this.searchCity;
                this.$http.get(url).then(function (response) {
                    this.clinics = response.data.content;
                    this.totalPages = response.data.totalPages;
                    this.page = response.data.pageable.pageNumber+1;
                })
            },
            getAllCities(){
                this.$http.get('/api/clinic/cities').then(function (response) {
                    this.cities = response.body;
                    this.cities.unshift("Все города");
                })
            },
            deleteClinic(id){
                this.$http.delete('/api/clinic?id='+id).then(function (response) {
                    window.location.href = '/clinic';
                })
            }
        },
        created() {
            this.getAllClinics(this.page);
            this.getAllCities();
        }
    }
</script>

<style scoped>

</style>