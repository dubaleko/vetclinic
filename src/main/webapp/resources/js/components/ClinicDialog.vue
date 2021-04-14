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
                    Название клиники не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.name.$dirty && !name.match('^[a-zA-Zа-яА-ЯёЁ0-9- ]+$')">
                    Название допускет кириллицу и буквы латинского алфавита, цифры и тире</h5>
                <v-text-field v-model="name" placeholder="Введите название клиники" label="Название клиники"/>
                <h5 class="validationError" v-if="!$v.description.required && $v.description.$dirty">
                    Описние клиники не может быть пустым</h5>
                <v-text-field v-model="description" placeholder="Введите описание клиники" label="Описание клиники"/>
                <h5 class="validationError" v-if="!$v.city.required && $v.city.$dirty">
                    Название города не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.city.$dirty && !city.match('^[а-яА-ЯёЁ ]+$')">
                    Имя города допускает только буквы кириллицы</h5>
                <v-text-field v-model="city" placeholder="Введите имя города" label="Город"/>
                <h5 class="validationError" v-if="!$v.address.required && $v.address.$dirty">
                    Адрес не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.address.$dirty && !address.match('^[а-яА-ЯёЁ0-9 ]+$')">
                    Адрес допускает буквы кириллицы и цифры </h5>
                <v-text-field v-model="address" placeholder="Введите адрес клиники" label="Адрес клиники"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="temporarySave">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="dialog=false">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    export default {
        props:['action','clinic'],
        name: "ClinicDialog",
        data: () => ({
            dialog: false, myClinic: null, id : '', description : '',
            name: '', city: '', address : ''
        }),
        validations:{
            description: {required}, name: {required},
            city: {required}, address : {required}
        },
        updated(){
            if (this.myClinic != this.clinic) {
                this.id = this.clinic.id;
                this.name = this.clinic.name;
                this.description = this.clinic.description;
                this.city = this.clinic.city;
                this.address = this.clinic.address;
                this.myClinic = this.clinic;
            }
        },
        methods : {
            temporarySave(){
                this.$v.$touch();
                if (this.$v.$invalid || !this.name.match('^[a-zA-Zа-яА-ЯёЁ0-9- ]+$') ||
                    !this.city.match('^[а-яА-ЯёЁ ]+$') || !this.address.match('^[а-яА-ЯёЁ0-9 ]+$')){
                    return;
                }else {
                    let clinic = {id : this.id ,name: this.name, description: this.description,
                        city: this.city, address: this.address};
                    if (this.action == "Добавить новую клинику"){
                        this.$http.post('/api/clinic',clinic).then(function (response) {
                            window.location.href = '/clinic';
                        })
                    }
                    else {
                        this.$http.put('/api/clinic',clinic).then(function (response) {
                            window.location.href = '/clinic';
                        })
                    }
                }
            }
        }
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>