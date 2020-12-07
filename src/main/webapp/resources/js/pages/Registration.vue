<template>
    <v-container fill height>
        <h6 class="validationError" v-if="!$v.userName.required && $v.userName.$dirty">Поле имя не может быть пустым</h6>
        <h6 class="validationError" v-else-if="$v.userName.$dirty && !$v.userName.minLength">Минимальная длина имени 4 символа</h6>
        <h6 class="validationError" v-else-if="$v.userName.$dirty && !userName.match('^[а-яА-ЯёЁa-zA-Z0-9]+$')">Им может состоять только из букв и цифр</h6>
        <v-text-field label="Имя пользователя" placeholder="Введите имя пользователя" v-model.trim="userName"/>
        <h6 class="validationError" v-if="!$v.password.required && $v.password.$dirty">Поле пароль не может быть пустым</h6>
        <h6 class="validationError" v-else-if="$v.password.$dirty && !$v.password.minLength">Минимальная длина пароля 6 символов</h6>
        <v-text-field :type="'password'" label="Пароль" placeholder="Введите пароль" v-model="password"/>
        <v-row justify="center">
            <v-btn @click="reg" class=" mr-10">Регистрация</v-btn>
            <v-btn v-bind:to="'/login'">Назад</v-btn>
        </v-row>
    </v-container>
</template>

<script>
    import router from "../router";
    import { required, minLength} from 'vuelidate/lib/validators'
    export default {
        name: "Registration",
        data(){
            return{
                userName: '', password: ''
            }
        },
        validations:{
            userName: {required, minLength: minLength(4)},
            password: {required, minLength: minLength(6)}
        },
        methods:{
            reg(){
                this.$v.$touch()
                if (this.$v.$invalid || !this.userName.match('^[а-яА-ЯёЁa-zA-Z0-9]+$')){
                    return
                }
                else {
                    let user = {userName: this.userName, password: this.password}
                    this.$http.post('/api/users', user).then(function (response) {
                        router.replace('/login');
                    })
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