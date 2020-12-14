<template>
    <div class="centerDiv">
        <h4>Эта страница доступна только зарегистрированным пользователям</h4>
        <div class="littleDiv">
            <login-dialog/>
            <registration-dialog/>
        </div>
    </div>
</template>

<script>
    import LoginDialog from "../components/LoginDialog.vue";
    import RegistrationDialog from "../components/RegistrationDialog.vue";
    export default {
        name: "Authenticated",
        components: {LoginDialog,RegistrationDialog},
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