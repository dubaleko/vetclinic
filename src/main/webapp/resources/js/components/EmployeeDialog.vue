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
                    Полное имя не может быть пустым</h5>
                <h5 class="validationError" v-else-if="$v.name.$dirty && !name.match(onlyChar)">
                    Полное имя допускает только буквы кириллицы</h5>
                <v-text-field v-model="name" placeholder="Введите полное имя" label="Полное имя"/>
                <h5 class="validationError" v-if="!$v.position.required && $v.position.$dirty">
                    Данные об должности не могут быть пустыми</h5>
                <h5 class="validationError" v-else-if="$v.position.$dirty && !position.match(onlyChar)">
                    Должность допускает только буквы кириллицы</h5>
                <v-text-field v-model="position" placeholder="Введите данные об должности" label="Должность"/>
                <h5 class="validationError" v-if="!$v.education.required && $v.education.$dirty">
                    Данные об образовании не могут быть пустыми</h5>
                <v-text-field v-model="education" placeholder="Введите данные об образовании" label="Образование"/>
                <h5 class="validationError" v-if="!$v.clinicName.required && $v.clinicName.$dirty">
                    Название клиники не может быть пустым</h5>
                <v-select v-if="user.role == 'ADMIN'" v-model="clinicName" :items="clinicsName" label="Название клиники"/>
                <h5 class="validationError" v-if="!$v.employeeSpecName.required && $v.employeeSpecName.$dirty">
                    Специализация не может быть пустой</h5>
                <v-select multiple chips v-model="employeeSpecName" :items="specName" label="Специализация"/>
                <h5 class="validationError" v-if="$v.employeeWorkDay.$dirty && this.employeeWorkDay.length == 0
                                            && !this.vacation">Должен быть хотя бы 1 рабочий день</h5>
                <v-select :disabled="vacation" multiple chips v-model="employeeWorkDay" :items="daysName" label="Рабочие дни"/>
                <h5 class="validationError" v-if="$v.startWork.$dirty && this.startWork >= this.endWork && !this.vacation
                                                 && this.startWork && this.endWork">
                    Рабочий день не может начинатся позже чем заканчивается</h5>
                <v-row align="center">
                    <v-col >
                        <h5 class="validationError" v-if="$v.startWork.$dirty && !this.startWork && !this.vacation">
                            Должно быть время начала рабочего дня</h5>
                        <v-select :disabled="vacation" v-model="startWork" :items="timesValues"
                                  label="Начало рабочего дня"/>
                    </v-col>
                    <v-col>
                        <h5 class="validationError" v-if="$v.endWork.$dirty && !this.endWork && !this.vacation">
                            Должно быть время конца рабочего дня</h5>
                        <v-select :disabled="vacation"  v-model="endWork" :items="timesValues"
                                  label="Конец рабочего дня"/>
                    </v-col>
                </v-row>
                <input v-model="vacation" type="checkbox"> Сотрудник не доступен для записи онлайн
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
        name: "EmployeeDialog",
        props:['action','specName','spec','employee', 'user','clinics','clinicsName'],
        data: () => ({
            dialog: false,  myEmployee: null, onlyChar: '^[а-яА-ЯёЁ ]+$', id: '', name: '',
            clinic: '', clinicName:'', position : '', education: '', vacation: false, startWork:null,
            employeeWorkDay : [], employeeSpecName : [], times: [], timesValues:[], endWork:null,
            daysName :['Понедельник','Вторник','Среда','Четверг', 'Пятница','Суббота','Воскресенье']
        }),
        validations:{
            name: {required}, education: {required}, employeeWorkDay: {}, position: {required},
            employeeSpecName: {required}, clinicName: {required}, startWork:{}, endWork:{}
        },
        watch:{
            vacation: function (newTemplate, oldTemplate) {
                if (newTemplate != oldTemplate){
                    if (this.vacation) {
                        this.employeeWorkDay = [];
                        this.startWork = this.endWork = null;
                    }
                }
            },
        },
        updated(){
            if (this.myEmployee != this.employee) {
                this.id = this.employee.id;
                this.name = this.employee.name;
                this.position = this.employee.position;
                this.education = this.employee.education;
                if (this.user.role == 'ADMIN'){
                    this.clinicName = this.employee.clinic.name;
                }
                else {
                    this.clinic = this.employee.clinic;
                }
                if (this.employee.specs) {
                    this.employee.specs.forEach(element => {
                        this.employeeSpecName.push(element.name);
                    })
                }
                if (this.employee.days){
                    this.employee.days.forEach(element =>{
                        this.employeeWorkDay.push(element.dayName);
                    })
                }
                this.vacation = this.employee.onVacation;
                if (this.employee.startWork) {
                    this.startWork = this.employee.startWork.time;
                }
                if (this.employee.endWork) {
                    this.endWork = this.employee.endWork.time;
                }
                this.myEmployee = this.employee;
            }
        },
        methods : {
            save(){
                if (this.user.role == 'ADMIN'){
                    this.clinic = getObjectByName(this.clinics,this.clinicName);
                }
                else {
                    this.clinicName = 'Имя';
                    this.clinic = this.user.clinic;
                }
                this.$v.$touch();
                if (this.$v.$invalid || !this.name.match(this.onlyChar) || !this.position.match(this.onlyChar) ||
                    this.employeeWorkDay.length == 0 && !this.vacation || !this.startWork && !this.vacation ||
                    !this.endWork && !this.vacation || this.startWork >= this.endWork  && !this.vacation ){
                    return
                }else {
                    let employeeSpecs = [];
                    let employeeDays = [];
                    this.spec.forEach(element=>{
                        this.employeeSpecName.forEach(specName=>{
                            if (element.name == specName){
                                employeeSpecs.push(element);
                            }
                        })
                    })
                    this.employeeWorkDay.forEach(dayName=>{
                        employeeDays.push({id:null,dayName:dayName,employees:null});
                    })
                    let startWortTime, endWorkTime;
                    this.times.forEach(element=>{
                        if (element.time == this.startWork){
                            startWortTime = element;
                        }
                        else if (element.time == this.endWork){
                            endWorkTime = element;
                        }
                    });
                    let employee = {id : this.id ,name: this.name, position:this.position,clinic: this.clinic,
                        education: this.education, specs: employeeSpecs,days:employeeDays, startWork: startWortTime,
                        endWork: endWorkTime, onVacation: this.vacation};
                    if (this.action == "Добавить нового сотрудника"){
                        this.$http.post('/api/employee',employee).then(function (response) {
                            window.location.href = '/employee';
                        })
                    }
                    else {
                        this.$http.put('/api/employee',employee).then(function (response) {
                            window.location.href = '/employee';
                        })
                    }
                }
            }
        },
        created() {
            this.$http.get('/api/employee/workTime').then(function (response) {
                this.times = response.data;
                this.times.forEach(element=>{
                    this.timesValues.push(element.time);
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