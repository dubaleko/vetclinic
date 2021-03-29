<template>
    <v-app>
        <v-app-bar app color="blue">
            <v-btn color="white" text href="/">
                <v-toolbar-title>Братья Меньшие</v-toolbar-title>
            </v-btn>
            <v-btn text color="white" class="ml-12" href="/clinic">Клиники</v-btn>
            <v-btn text color="white" class="ml-2" href="/service">Услуги</v-btn>
            <v-btn text color="white" class="ml-2" href="/employee">Сотрудники</v-btn>
            <v-btn text color="white" class="ml-2" href="/reviews">Отзывы</v-btn>
            <v-btn text color="white" class="ml-2" href="/order">Запись на прием</v-btn>
            <v-btn text color="white" class="ml-2" href="/talons">Талоны</v-btn>
            <v-spacer></v-spacer>
            <div v-if="user">
                <span class="white--text">{{user.userName}}</span>
                <v-btn color="white" href="/logout" icon>
                    <v-icon>exit_to_app</v-icon>
                </v-btn>
            </div>
            <div v-if="!user">
                <login-dialog/>
                <registration-dialog/>
            </div>
        </v-app-bar>
        <v-main>
            <router-view :user="user" ></router-view>
        </v-main>
    </v-app>
</template>

<script>
    import LoginDialog from "../components/LoginDialog.vue";
    import RegistrationDialog from "../components/RegistrationDialog.vue";
    export default {
        components: {LoginDialog,RegistrationDialog},
        data(){
            return{
                user:null
            }
        },
        methods:{
            getUser(){
                this.$http.get('/api/users/current').then(function (response) {
                    if (response.data.userName != null){
                        this.user = response.data;
                    }
                })
            },
        },
        created() {
            this.getUser();
        }
    }
</script>

<style scoped>
</style>
