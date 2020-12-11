<template>
    <v-app>
        <v-app-bar app>
            <v-spacer></v-spacer>
            <v-spacer v-if="!user"></v-spacer>
            <v-btn text href="/">
                <v-toolbar-title>Братья Меньшие</v-toolbar-title>
            </v-btn>
            <v-spacer></v-spacer>
            <div v-if="user">
                <span>{{user.userName}}</span>
                <v-btn href="/logout" icon>
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
                this.$http.get('/api/users/').then(function (response) {
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