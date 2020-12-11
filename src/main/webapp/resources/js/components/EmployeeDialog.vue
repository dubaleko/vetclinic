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
                <h5 class="validationError" v-if="!$v.employeeSpecName.required && $v.employeeSpecName.$dirty">
                    Специализация не может быть пустой</h5>
                <v-select multiple chips v-model="employeeSpecName" :items="specName" label="Специализация"/>
                <h5 class="validationError" v-if="!$v.employeeWorkDay.required && $v.employeeWorkDay.$dirty">
                    У сотрудника должен быть хотя бы 1 рабочий день</h5>
                <v-select multiple chips v-model="employeeWorkDay" :items="daysName" label="Рабочие дни"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="temporarySave">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "EmployeeDialog",
        props:['action','specName','spec','employee'],
        data: () => ({
            dialog: false,  position : '', education: '', employeeWorkDay : [],
            id: '', name: '',  onlyChar: '^[а-яА-ЯёЁ ]+$', myEmployee: null,
            employeeSpecName : [], daysName :['Понедельник','Вторник','Среда','Четверг',
            'Пятница','Суббота','Воскресенье']
        }),
        validations:{
            name: {required}, education: {required}, employeeWorkDay: {required},
            position: {required}, employeeSpecName: {required}
        },
        updated(){
            if (this.myEmployee != this.employee) {
                this.id = this.employee.id;
                this.name = this.employee.name;
                this.position = this.employee.position;
                this.education = this.employee.education;
                if (this.employee.specs) {
                    this.employee.specs.forEach(element => {
                        this.employeeSpecName.push(element.specialization);
                    })
                }
                if (this.employee.days){
                    this.employee.days.forEach(element =>{
                        this.employeeWorkDay.push(element.dayName);
                    })
                }
                this.myEmployee = this.employee;
            }
        },
        methods : {
            close(){
                this.dialog = false;
            },
            temporarySave(){
                this.$v.$touch()
                if (this.$v.$invalid || !this.name.match(this.onlyChar) || !this.position.match(this.onlyChar)){
                    return
                }else {
                    let employeeSpecs = [];
                    let employeeDays = [];
                    this.spec.forEach(element=>{
                        this.employeeSpecName.forEach(specName=>{
                            if (element.specialization == specName){
                                employeeSpecs.push(element);
                            }
                        })
                    })
                    this.employeeWorkDay.forEach(dayName=>{
                        let object = {id:null,dayName:dayName,employees:null}
                        employeeDays.push(object);
                    })
                    let employee = {id : this.id ,name: this.name, position:this.position,
                        education: this.education, specs: employeeSpecs,days:employeeDays};
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
        }
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>