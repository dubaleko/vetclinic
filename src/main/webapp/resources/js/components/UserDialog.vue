<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn text  v-on="on">Обновить</v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="headline">Обновить</span>
            </v-card-title>
            <v-card-text>
                <h5 class="validationError" v-if="!$v.name.required && $v.name.$dirty">
                    Имя пользователя не может быть пустыым</h5>
                <v-text-field v-model="name" placeholder="Введите имя пользователя" label="Имя пользователя"/>
                <v-select v-model="role" :items="roles" label="Роль"/>
                <h5 class="validationError" v-if="$v.clinic.$dirty && role == 'MODERATOR' && !clinic">
                    Выберите клинику</h5>
                <v-select v-if="role == 'MODERATOR'" v-model="clinic" :items="clinicsName" label="Ответсвенный за клинику"/>
                <h5 class="validationError" v-if="$v.doctor.$dirty && role == 'DOCTOR' && !doctor">
                    Выберите врача</h5>
                <v-select v-if="role == 'DOCTOR'" v-model="doctor" :items="doctorsName" label="Врач"/>
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
        props:['action','user'],
        name: "UserDialog",
        data(){
            return{
                dialog: false, roles : ['ADMIN','USER','MODERATOR','DOCTOR'],
                clinics: [], doctors:[], clinicsName:[], doctorsName:[],
                myUser: '',  name: '', role: '', clinic:null, doctor: null
            }
        },
        validations:{
            name : {required}, clinic: {}, doctor: {},
        },
        updated(){
            if (this.myUser != this.user) {
                this.name = this.user.userName;
                this.role = this.user.role;
                if (this.user.clinic) {
                    this.clinic = this.user.clinic.name;
                }
                if (this.user.doctor) {
                    this.doctor = this.user.doctor.name;
                }
                this.myUser = this.user;
            }
        },
        methods : {
            save(){
                this.$v.$touch();
                if (this.$v.$invalid || this.role == 'MODERATOR' && !this.clinic
                    || this.role == 'DOCTOR' && !this.doctor ){
                    return
                }else {
                    if (this.role =='MODERATOR'){
                        this.clinic = getObjectByName(this.clinics,this.clinic);
                        this.doctor = null;
                    }
                    else if (this.role == 'DOCTOR'){
                        this.doctor = getObjectByName(this.doctors,this.doctor);
                        this.clinic = null;
                    }
                    else {
                        this.clinic = this.doctor = null;
                    }
                    let user = {id: this.user.id, userName: this.name, password: this.user.password,
                        role: this.role, clinic: this.clinic, doctor: this.doctor, petName: this.user.petName,
                        petType: this.user.petType, petAge: this.user.petAge, petGender: this.user.petGender,
                        ownerName: this.user.ownerName, ownerSecondName: this.user.ownerSecondName};
                    this.$http.put('/api/users',user).then(function (response) {
                        window.location.href = '/users';
                    })
                }
            }
        },
        created() {
            this.$http.get('/api/employee/all').then(function (response) {
                this.doctors = response.body;
                this.doctors.forEach(doctor=>{
                    this.doctorsName.push(doctor.name);
                })
            })
            this.$http.get('/api/clinic/all').then(function (response) {
                this.clinics = response.body;
                this.clinics.forEach(clinic=>{
                    this.clinicsName.push(clinic.name);
                })
            })
        }
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>