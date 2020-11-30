<template>
    <v-app>
        <v-app-bar app>
            <v-spacer></v-spacer>
            <v-btn text href="/">
                <v-toolbar-title>Братья Меньшие</v-toolbar-title>
            </v-btn>
            <v-spacer></v-spacer>
            <div v-if="!loginPage">
                <div v-if="user">
                    <span>{{user.userName}}</span>
                    <v-btn href="/logout" icon>
                        <v-icon>exit_to_app</v-icon>
                    </v-btn>
                </div>
                <div v-if="!user">
                    <span>Войти</span>
                    <v-btn href="/login" icon>
                        <v-icon>login</v-icon>
                    </v-btn>
                </div>
            </div>
        </v-app-bar>
        <v-main>
            <router-view :user="user" ></router-view>
        </v-main>
    </v-app>
</template>

<script>
    export default {
        data(){
            return{
                loginPage:false,
                user:null
            }
        },
        methods:{
            getUser(){
                this.$http.get('/api/users/').then(function (response) {
                    if (response.data.userName != null){
                        this.user = response.data;
                    }
                })
            },
        },
        created() {
            if (window.location.pathname == "/login" || window.location.pathname == "/registration"){
                this.loginPage = true;
            }
            this.getUser();
        }
    }
</script>

<style scoped>

</style>