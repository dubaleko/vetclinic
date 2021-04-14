<template>
    <v-card flat>
        <v-card-title><h3>Мероприятия проводимые с питомцем</h3></v-card-title>
        <v-card-text>
            <div class="mb-4" v-if="show">
                <pet-events-dialog v-if="show"  :user="user" action="Добавить новое мероприятие"/>
            </div>
            <v-expansion-panels>
                <v-expansion-panel class="mb-4"  v-for="petEvent in petEvents" :key="petEvent.id">
                    <v-expansion-panel-header>
                        <div>{{petEvent.name}}</div>
                        {{petEvent.dateFormatted}}
                    </v-expansion-panel-header>
                    <v-expansion-panel-content v-if="show">
                        <pet-events-dialog :event="petEvent" :user="user" action="Обновить"/>
                        <v-btn text @click="deletePetEvent(petEvent.id)">Удалить</v-btn>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-expansion-panels>
            <v-row align="center" justify="center" v-if="emptyEvents">
                Информация о мероприятиях проводимых с питомцем отсуствует
            </v-row>
            <v-pagination v-if="totalEventPages > 1" @input="getAllEvents(this.eventPage)" v-model="eventPage"
                          :length="totalEventPages" :total-visible="7" prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </v-card-text>
    </v-card>
</template>

<script>
    import {formatDate} from "../methods.js";
    import PetEventsDialog from "./PetEventsDialog.vue";
    export default {
        name: "PetEvent",
        components: {PetEventsDialog},
        props: ['user', 'show'],
        data() {
            return{
                petEvents:[], emptyEvents:false, eventPage : null, totalEventPages: null
            }
        },
        methods: {
            getAllEvents(page){
                if (!page)
                    page = 1;
                this.$http.get('/api/petEvent?id='+this.user.id+'&page='+page).then(function (response) {
                    this.petEvents = response.data.content;
                    this.totalEventPages = response.data.totalPages;
                    this.eventPage = response.data.pageable.pageNumber+1;
                    this.emptyEvents = false;
                    if (this.petEvents.length < 1){
                        this.emptyEvents = true;
                    }
                    this.petEvents.forEach(element=>{
                        element.dateFormatted = formatDate(element.date);
                    })
                })
            },
            deletePetEvent(id){
                this.$http.delete('/api/petEvent?id='+id).then(function (response) {
                    window.location.href = '/me';
                })
            },
        },
        created() {
            this.getAllEvents(this.eventPage);
        }
    }
</script>

<style scoped>

</style>