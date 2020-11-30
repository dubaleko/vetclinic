<template>
    <v-container fill height>
        <h4 class="validationError"  v-if="errorMessage" v-text="errorMessage"/>
        <v-text-field label="Имя пользователя" placeholder="Введите имя пользователя" v-model="userName"/>
        <v-text-field :type="'password'" label="Пароль" placeholder="Введите пароль" v-model="password"  />
        <v-row justify="center">
            <v-btn class="mr-5" @click="getUser">Войти</v-btn>
            <v-btn v-bind:to="'/registration'">Зарегистрироваться</v-btn>
        </v-row>
    </v-container>
</template>

<script>
    export default {
        name: "Login",
        data(){
            return{
                userName: null,
                password: null,
                errorMessage:null
            }
        },
        methods:{
            getUser(){
                this.$http.post('/login?username='+this.userName+'&password='+this.password).then((response) => {
                    window.location.href='/';
                }, (response)=>{
                    this.errorMessage = "Wrong username or password"
                })
            }
        },
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>