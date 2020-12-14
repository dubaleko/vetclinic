<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn v-on="on">Войти</v-btn>
        </template>
        <v-card>
            <v-card-title>
                Войти
            </v-card-title>
            <v-card-text>
                <h4 class="validationError"  v-if="errorMessage" v-text="errorMessage"/>
                <v-text-field label="Имя пользователя" placeholder="Введите имя пользователя" v-model="userName"/>
                <v-text-field :type="'password'" label="Пароль" placeholder="Введите пароль" v-model="password"  />
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="getUser()">Войти</v-btn>
                <v-btn color="blue darken-1" text @click="close">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        name: "LoginDialog",
        data: () => ({
            dialog: false,errorMessage:null,
            userName: null, password: null,
        }),
        methods:{
            close(){
                this.dialog = false;
            },
            getUser(){
                this.$http.post('/login?username='+this.userName+'&password='+this.password).then((response) => {
                    window.location.href='/';
                }, (response)=>{
                    this.errorMessage = "Неправильные имя пользователя или пароль"
                })
            }
        }
    }
</script>

<style scoped>
    .validationError{
        color: red;
    }
</style>