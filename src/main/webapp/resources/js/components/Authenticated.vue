<template>
    <div class="centerDiv">
        Эта страница доступна только зарегистрированным пользователям
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
        font-size: 20px;
        position: absolute;
        top: 44%;
        left: 51%;
        margin-right: -45%;
        transform: translate(-50%, -50%)
    }
    .littleDiv{
        position: absolute;
        top: 200%;
        left: 32%;
    }
</style>