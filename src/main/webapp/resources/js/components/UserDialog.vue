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
                <v-text-field v-model="name" placeholder="Введите имя пользователя" label="Имя пользователя"/>
                <v-select v-model="role" :items="roles" label="Роль"/>
                <v-select v-if="role == 'MODERATOR'" v-model="clinic" :items="clinicsName" label="Ответсвенный за клинику"/>
                <v-select v-if="role == 'DOCTOR'" v-model="doctor" :items="doctorsName" label="Врач"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="save">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    export default {
        props:['action','user'],
        name: "UserDialog",
        data(){
            return{
                dialog: false, roles : ['ADMIN','USER','MODERATOR','DOCTOR'],
                clinics: [], doctors:[], clinicsName:[], doctorsName:[],
                myUser: '', id: '', name: '', role: '', clinic:null, doctor: null
            }
        },
        validations:{
            name : {required}
        },
        updated(){
            if (this.myUser != this.user) {
                if (this.user.id) {
                    this.id = this.user.id;
                    this.name = this.user.userName;
                    this.role = this.user.role;
                    if (this.user.clinic) {
                        this.clinic = this.user.clinic.name;
                    }
                    if (this.user.doctor) {
                        this.doctor = this.user.doctor.name;
                    }
                }
                this.myUser = this.user;
            }
        },
        methods : {
            close(){
                this.dialog = false;
            },
            save(){
                if (this.role =='MODERATOR'){
                    this.clinics.forEach(clinic=>{
                        if (clinic.name == this.clinic){
                            this.clinic = clinic;
                        }
                    })
                    this.doctor = null;
                }
                else if (this.role == 'DOCTOR'){
                    this.doctors.forEach(doctor=>{
                        if (doctor.name == this.doctor){
                            this.doctor = doctor;
                        }
                    })
                    console.log(this.doctor);
                    this.clinic = null;
                }
                else {
                    this.clinic = this.doctor = null;
                }
                let user = {id: this.id, userName: this.name, password: this.myUser.password,
                    role: this.role, clinic: this.clinic, doctor: this.doctor};
                this.dialog = false;
                this.$http.put('/api/users',user).then(function (response) {
                    window.location.href = '/users';
                })
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

</style>