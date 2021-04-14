<template>
        <v-dialog v-model="dialog" fullscreen>
            <template v-slot:activator="{ on }">
                <v-btn text  v-on="on">Карточка</v-btn>
            </template>
            <v-card>
                <v-toolbar dark color="primary">
                    <v-btn icon dark @click="dialog = false">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title>Карточка питомца</v-toolbar-title>
                </v-toolbar>
                <v-tabs v-model="tab" grow>
                    <v-tab v-for="item in items" :key="item">
                        {{ item }}
                    </v-tab>
                </v-tabs>
                <v-tabs-items v-model="tab">
                    <v-tab-item>
                        <v-card flat>
                            <v-card-title><h3>Основная информация о питомце</h3></v-card-title>
                            <v-card-text>
                                <v-banner elevation="0">Кличка: {{user.petName}}</v-banner>
                                <v-banner>Вид животного: {{user.petType}}</v-banner>
                                <v-banner>Полных лет: {{user.petAge}}</v-banner>
                                <v-banner>Пол животного: {{user.petGender}}</v-banner>
                                <v-banner>Владелец: {{user.ownerSecondName}} {{user.ownerName}}</v-banner>
                            </v-card-text>
                        </v-card>
                    </v-tab-item>
                    <v-tab-item>
                        <pet-event :user="user"/>
                    </v-tab-item>
                    <v-tab-item>
                        <visit-history :order="order" :user="user"/>
                    </v-tab-item>
                </v-tabs-items>
            </v-card>
        </v-dialog>
</template>

<script>
    import PetEvent from "./PetEvent.vue";
    import VisitHistory from "./VisitHistory.vue";
    export default {
        name: "card",
        components: {VisitHistory, PetEvent},
        props:['user','order'],
        data(){
            return{
                dialog: false,  tab:'', items:[ 'Основные','Проводимые мероприятия', 'История посещений'],
            }
        }
    }
</script>

<style scoped>

</style>