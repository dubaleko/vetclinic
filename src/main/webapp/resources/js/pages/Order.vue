<template>
    <v-container v-if="!user" fill-height>
        <div class="centerDiv">
            <h4>Эта страница доступна только зарегистрированным пользователям</h4>
            <div class="littleDiv">
                <login-dialog/>
                <registration-dialog/>
            </div>
        </div>
    </v-container>
    <v-container v-else-if="user">
        <h1>Запись на прием</h1>
        <h1>{{employee}}</h1>
    </v-container>
</template>

<script>
    import LoginDialog from "../components/LoginDialog.vue";
    import RegistrationDialog from "../components/RegistrationDialog.vue";
    export default {
        name: "Order",
        components: {LoginDialog,RegistrationDialog},
        props:['user','employee'],
        created() {
            console.log(this.$route.query.test)
        }
    }
</script>

<style scoped>
    .centerDiv{
        position: absolute;
        top: 40%;
        left: 50%;
        margin-right: -45%;
        transform: translate(-50%, -50%)
    }
    .littleDiv{
        position: absolute;
        top: 200%;
        left: 25%;
    }
</style>