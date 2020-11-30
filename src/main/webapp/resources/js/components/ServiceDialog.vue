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
                <h5 class="validationError" v-if="!$v.serviceType.required && $v.serviceType.$dirty">Тип услуги не может быть пустым</h5>
                <v-select v-model="serviceType" :items="typeNames" label="Тип услуги"/>
                <h5 class="validationError" v-if="!$v.serviceName.required && $v.serviceName.$dirty">Имя сервиса не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.serviceName.$dirty && !serviceName.match('^[а-яА-ЯёЁ0-9()-/ ]+$')">Имя допускает буквы кириллицы, цифры, круглые скобки, тире и косую черту</h5>
                <v-text-field v-model="serviceName" placeholder="Введите имя услуги" label="Имя услуги"/>
                <h5 class="validationError" v-if="!$v.serviceCost.required && $v.serviceCost.$dirty">Цена услуги не может быть пустой</h5>
                <h5 class="validationError" v-else-if="$v.serviceCost.$dirty && !serviceCost.match('^[0-9]*[.]?[0-9]+$')">Цена услуги может быть только числом</h5>
                <v-text-field v-model="serviceCost" placeholder="Введите цену услуги" label="Цена услуги"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="temporarySave">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required ,} from 'vuelidate/lib/validators'
    export default {
        props:['action','types','typeNames','service'],
        name: "ServiceDialog",
        data: () => ({
            dialog: false, id : '', errorMessage : '', myService: null,
            serviceType : '', serviceName: '', serviceCost: ''
        }),
        validations:{
            serviceType: {required ,},
            serviceName: {required},
            serviceCost: {required},
        },
        updated(){
            if (this.myService != this.service) {
                if (this.service.serviceType) {
                    this.serviceType = this.service.serviceType.serviceTypeName;
                    this.id = this.service.id;
                    this.serviceName = this.service.serviceName;
                    this.serviceCost = this.service.serviceCost.toString();
                }
                this.myService = this.service;
            }
        },
        methods :{
            close(){
                this.errorMessage = '';
                this.dialog = false;
            },
            temporarySave(){
                this.$v.$touch()
                if (this.$v.$invalid || !this.serviceName.match('^[а-яА-ЯёЁ0-9()-/ ]+$')
                    || !this.serviceCost.toString().match('^[0-9]*[.]?[0-9]+$')){
                    return
                }else {
                    let serviceType = null;
                    this.types.forEach(element=>{
                        if (element.serviceTypeName == this.serviceType){
                            serviceType = element;
                        }
                    })
                    let service = {id : this.id ,serviceName: this.serviceName, serviceCost: this.serviceCost,serviceType:serviceType}
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
        },
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>