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
                <h5 class="validationError" v-if="!$v.clinicName.required && $v.clinicName.$dirty">
                    Название клиники не может быть пустым</h5>
                <v-select v-if="user.role == 'ADMIN'" v-model="clinicName" :items="clinicsName" label="Название клиники"/>
                <h5 class="validationError" v-if="!$v.serviceType.required && $v.serviceType.$dirty">
                    Тип услуги не может быть пустым</h5>
                <v-select v-model="serviceType" :items="typeNames" label="Тип услуги"/>
                <h5 class="validationError" v-if="!$v.serviceName.required && $v.serviceName.$dirty">
                    Название услуги не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.serviceName.$dirty && !serviceName.match('^[а-яА-ЯёЁ0-9()-/ ]+$')">
                    Название услуги допускает буквы кириллицы, цифры, круглые скобки, тире и косую черту</h5>
                <v-text-field v-model="serviceName" placeholder="Введите название услуги" label="Название услуги"/>
                <h5 class="validationError" v-if="!$v.serviceCost.required && $v.serviceCost.$dirty">
                    Цена услуги не может быть пустой</h5>
                <h5 class="validationError" v-else-if="$v.serviceCost.$dirty && !serviceCost.match('^[0-9]*[.]?[0-9]+$')">
                    Цена услуги может быть только одним числом</h5>
                <v-text-field v-model="serviceCost" placeholder="Введите цену услуги" label="Цена услуги"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="save">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="dialog=false">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    import {getObjectByName} from "../methods.js";
    export default {
        props:['action','types','typeNames','service','user','clinics','clinicsName'],
        name: "ServiceDialog",
        data: () => ({
            dialog: false, id : '', myService: null, serviceType : '',
            serviceName: '', serviceCost: '', clinicName: '',clinic:''
        }),
        validations:{
            serviceType: {required},
            serviceName: {required},
            serviceCost: {required},
            clinicName: {required}
        },
        updated(){
            if (this.myService != this.service) {
                this.serviceType = this.service.serviceType.name;
                this.id = this.service.id;
                if (this.user.role == 'ADMIN'){
                    this.clinicName = this.service.clinic.name;
                }
                else {
                    this.clinic = this.service.clinic;
                }
                this.serviceName = this.service.serviceName;
                this.serviceCost = this.service.serviceCost.toString();
                this.myService = this.service;
            }
        },
        methods :{
            save(){
                if (this.user.role == 'ADMIN'){
                    this.clinic = getObjectByName(this.clinics,this.clinicName);
                }
                else {
                    this.clinicName = 'Имя';
                    this.clinic = this.user.clinic;
                }
                this.$v.$touch();
                if (this.$v.$invalid || !this.serviceName.match('^[а-яА-ЯёЁ0-9()-/ ]+$')
                    || !this.serviceCost.toString().match('^[0-9]*[.]?[0-9]+$')){
                    return
                }else {
                    let serviceType = getObjectByName(this.types,this.serviceType);
                    let service = {id : this.id ,serviceName: this.serviceName, serviceCost: this.serviceCost,
                        serviceType:serviceType, clinic: this.clinic}
                    if (this.action == "Добавить новую услугу"){
                        this.$http.post('/api/service',service).then(function (response) {
                            window.location.href = '/service';
                        })
                    }
                    else {
                        this.$http.put('/api/service',service).then(function (response) {
                            window.location.href = '/service';
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