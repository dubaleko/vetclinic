<template>
    <v-container v-if="!user" fill-height>
        <authenticated/>
    </v-container>
    <v-container v-else>
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
                        <h5 class="validationError" v-if="!$v.petName.required && $v.petName.$dirty">
                            Кличка животного не может быть пустой</h5>
                        <h5 class="validationError" v-else-if="$v.petName.$dirty && !petName.match(onlyChar)">
                            Кличка животного допускает только буквы кириллицы</h5>
                        <v-text-field v-model="petName"  placeholder="Введите кличку животного" label="Кличка"/>
                        <h5 class="validationError" v-if="!$v.type.required && $v.type.$dirty">
                            Вид животного не может быть пустым</h5>
                        <h5 class="validationError" v-else-if="$v.petName.$dirty && !type.match(onlyChar)">
                           Вид животного допускает только буквы кириллицы</h5>
                        <v-text-field v-model="type"  placeholder="Выберите вид животного" label="Вид животного"/>
                        <h5 class="validationError" v-if="!$v.age.required && $v.age.$dirty">
                            Количество лет не может быть пустым</h5>
                        <h5 class="validationError" v-else-if="$v.age.$dirty && !$v.age.numeric">
                           Количетсво лет должно быть цифрой</h5>
                        <v-text-field v-model="age"  placeholder="Количество полных лет" label="Полных лет"/>
                        <h5 class="validationError" v-if="!$v.gender.required && $v.gender.$dirty">
                            Выберите пол животного</h5>
                        <v-row>
                            <v-col>
                                <input v-model="gender" class="mr-1" type="radio" value="Самец"/>
                                <label class="mr-4">Самец</label>
                                <input v-model="gender" class="mr-1" type="radio" value="Самка" label="Самка"/>
                                <label>Самка</label>
                            </v-col>
                        </v-row>
                        <h5 class="validationError" v-if="!$v.ownerSecondName.required && $v.ownerSecondName.$dirty">
                            Фамилия владельца не может быть пустой</h5>
                        <h5 class="validationError" v-else-if="$v.ownerSecondName.$dirty && !ownerSecondName.match(onlyChar)">
                           Фамилия владельца допускает только буквы кириллицы</h5>
                        <v-text-field v-model="ownerSecondName" placeholder="Фамилия" label="Фамилия владельца"/>
                        <h5 class="validationError" v-if="!$v.ownerName.required && $v.ownerName.$dirty">
                           Имя владельца не может быть пустым</h5>
                        <h5 class="validationError" v-else-if="$v.ownerName.$dirty && !ownerName.match(onlyChar)">
                            Имя владельца допускает только буквы кириллицы</h5>
                        <v-text-field v-model="ownerName" placeholder="Имя" label="Имя владельца"/>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn class="white--text" color="blue darken-1" @click="save" >Сохранить</v-btn>
                        <v-btn class="white--text" color="blue darken-1" @click="setValue">Отменить</v-btn>
                    </v-card-actions>
                </v-card>
            </v-tab-item>
            <v-tab-item>
                <pet-event :show="true" :user="this.user"/>
            </v-tab-item>
            <v-tab-item>
                <visit-history :user="this.user"/>
            </v-tab-item>
        </v-tabs-items>
    </v-container>
</template>

<script>
    import Authenticated from "../components/Authenticated.vue";
    import {required ,numeric} from 'vuelidate/lib/validators'
    import VisitHistory from "../components/VisitHistory.vue";
    import PetEvent from "../components/PetEvent.vue";
    export default {
        name: "Me",
        components: {PetEvent, VisitHistory, Authenticated},
        data(){
            return{
                tab: '', items:[ 'Основные','Проводимые мероприятия', 'История посещений'],
                onlyChar: '^[а-яА-ЯёЁ ]+$',  user:'', gender:'', petName: '', type : '', age: '',
                ownerName: '', ownerSecondName:'',
            }
        },
        validations:{
            petName : {required},  type: {required}, age:{required, numeric},
            gender: {required}, ownerName:{required}, ownerSecondName: {required}
        },
        methods:{
            setValue(){
                this.petName = this.user.petName;
                this.type = this.user.petType;
                this.age = this.user.petAge;
                this.gender = this.user.petGender;
                this.ownerName = this.user.ownerName;
                this.ownerSecondName = this.user.ownerSecondName;
            },
            save(){
                this.$v.$touch();
                if (this.$v.$invalid || !this.petName.match(this.onlyChar)|| !this.type.match(this.onlyChar) ||
                    !this.ownerName.match(this.onlyChar) || !this.ownerSecondName.match(this.onlyChar)){
                    return
                }
                else {
                    let user = {id: this.user.id, userName: this.user.userName, password: this.user.password,
                        role: this.user.role, clinic: this.user.clinic, doctor: this.user.doctor,
                        petName: this.petName, petType: this.type, petAge: this.age, petGender: this.gender,
                        ownerName: this.ownerName, ownerSecondName: this.ownerSecondName};
                    this.$http.put('/api/users',user).then(function (response) {
                        window.location.href = '/me';
                    })
                }
            }
        },
        created() {
            this.$http.get('/api/users/current').then(function (response) {
                if (response.data.userName != null){
                    this.user = response.data;
                    this.setValue();
                }
            });
        }
    }
</script>

<style scoped>
    .validationError{color: red;}
</style>