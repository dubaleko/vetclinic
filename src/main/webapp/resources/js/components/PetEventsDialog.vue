<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn text  v-on="on">{{action}}</v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="headline" v-text="action"/>
            </v-card-title>
            <v-card-text>
                <h5 class="validationError" v-if="!$v.name.required && $v.name.$dirty">
                    Название мероприятия не может быть пустым</h5>
                <v-text-field v-model="name" label="Название мероприятия"/>
                <v-menu v-model="menu" :close-on-content-click="false" transition="scale-transition"
                        offset-y max-width="290px" min-width="auto">
                    <template v-slot:activator="{ on, attrs }">
                        <v-text-field v-model="dateFormatted" label="Дата мероприятия" v-bind="attrs" v-on="on"/>
                    </template>
                    <v-date-picker v-model="date" no-title :max="new Date().toISOString().substr(0, 10)" min="1950-01-01"
                            locale = "ru" @input="menu1 = false"/>
                </v-menu>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="save">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {formatDate} from "../methods.js";
    import {required} from 'vuelidate/lib/validators'
    import Me from "../pages/Me.vue";
    export default {
        props:['user','action','event'],
        name: "PetEventsDialog",
        data: () => ({
            dialog: false, id: null, name : null, myEvent: '',
            menu: false, date: null, dateFormatted: null
        }),
        updated(){
            if (this.event != this.myEvent) {
                if (this.event) {
                    this.id = this.event.id;
                    this.name = this.event.name;
                    this.date = this.event.date;
                    this.myEvent = this.event;
                }
            }
        },
        validations:{
            name: {required}
        },
        watch: {
            date: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    this.dateFormatted = formatDate(this.date)
                }
            }
        },
        created() {
            this.date = new Date().toISOString().substr(0, 10);
            this.dateFormatted = formatDate(new Date().toISOString().substr(0, 10));
        },
        methods :{
            close(){
                this.dialog = false;
            },
            save(){
                this.$v.$touch();
                if (this.$v.$invalid){
                    return
                }
                else {
                    let petEvent = {id: this.id, name: this.name, date: this.date, user: this.user};
                    if (this.action == 'Добавить новое мероприятие'){
                        this.$http.post('/api/petEvent',petEvent).then(function (response) {
                            window.location.href = '/me';
                        })
                    }
                    else {
                        this.$http.put('/api/petEvent',petEvent).then(function (response) {
                            window.location.href = '/me';
                        })
                    }
                }
            }
        }
    }
</script>

<style scoped>
    .validationError{color: red;}
</style>