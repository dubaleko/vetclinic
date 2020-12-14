<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn color="blue" class="white--text" v-on="on">Регистрация</v-btn>
        </template>
        <v-card>
            <v-card-title>
                Регистрация
            </v-card-title>
            <v-card-text>
                <h4 class="validationError"  v-if="responseCode == 409" v-text="responseStatus"/>
                <h6 class="validationError" v-if="!$v.userName.required && $v.userName.$dirty">Поле имя не может быть пустым</h6>
                <h6 class="validationError" v-else-if="$v.userName.$dirty && !$v.userName.minLength">Минимальная длина имени 4 символа</h6>
                <h6 class="validationError" v-else-if="$v.userName.$dirty && !userName.match('^[а-яА-ЯёЁa-zA-Z0-9]+$')">Им может состоять только из букв и цифр</h6>
                <v-text-field label="Имя пользователя" placeholder="Введите имя пользователя" v-model.trim="userName"/>
                <h6 class="validationError" v-if="!$v.password.required && $v.password.$dirty">Поле пароль не может быть пустым</h6>
                <h6 class="validationError" v-else-if="$v.password.$dirty && !$v.password.minLength">Минимальная длина пароля 6 символов</h6>
                <v-text-field :type="'password'" label="Пароль" placeholder="Введите пароль" v-model="password"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="reg()">Зарегистрироваться</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import { required, minLength} from 'vuelidate/lib/validators'
    export default {
        name: "LoginDialog",
        data: () => ({
            dialog: false, responseCode : null, responseStatus: null,
            userName: '', password: '',
        }),
        validations:{
            userName: {required, minLength: minLength(4)},
            password: {required, minLength: minLength(6)}
        },
        methods:{
            close(){
                this.dialog = false;
            },
            reg(){
                this.$v.$touch()
                if (this.$v.$invalid || !this.userName.match('^[а-яА-ЯёЁa-zA-Z0-9]+$')){
                    return
                }
                else {
                    let user = {userName: this.userName, password: this.password}
                    this.$http.post('/api/users', user).then(function (response) {
                        window.location.href='/';
                    }, (response) => {
                        console.log(response)
                        this.responseCode = response.status;
                        this.responseStatus = 'Пользователь с таким именем уже существует';
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