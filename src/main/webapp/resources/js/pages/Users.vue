<template>
    <v-container fill-height v-if="user == null || user.role != 'ADMIN'">
        <not-found/>
    </v-container>
    <v-container v-else>
        <h1>Пользователи</h1>
        <v-text-field v-model="name"  label="Имя пользователя"/>
        <table width="100%">
            <tr class="bottom-border" v-for="user in users" :key="user.id">
                <td align="left">{{user.userName}}</td>
                <td align="left">{{user.role}}</td>
                <td align="right">
                    <user-dialog :user="user"/>
                    <v-btn text @click="deleteUser(user.id)">Удалить</v-btn>
                </td>
            </tr>
        </table>
        <v-row align="center" justify="center" v-if="this.emptyUsers">
            Пользователей с подобным именем не было найдено
        </v-row>
        <v-pagination v-if="totalPages > 1" @input="getAllUsers" v-model="page" :length="totalPages" :total-visible="7"
                      prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
    </v-container>
</template>

<script>
    import UserDialog from "../components/UserDialog.vue";
    import NotFound from "./NotFound.vue";
    export default {
        name: "Users",
        components: {NotFound, UserDialog},
        props:['user'],
        data(){
            return {
                page: null, totalPages: null, emptyUsers: false, name: '', users: []
            }
        },
        watch: {
          name : function (newTemplate, oldTemplate) {
              if (newTemplate != oldTemplate){
                  this.page = 1;
                  this.getAllUsers();
              }
          }
        },
        methods:{
            getAllUsers(page){
                if(!page)
                    page = 1;
                let url = '/api/users?page='+page+'&name='+this.name;
                this.$http.get(url).then(function (response) {
                    this.users = response.body.pageList;
                    this.totalPages = response.body.pageCount;
                    this.page = response.body.page+1;
                    this.emptyUsers = false;
                    if (this.users.length < 1){
                        this.emptyUsers = true;
                    }
                })
            },
            deleteUser(id){
                this.$http.delete('/api/users?id='+id).then(function (response) {
                    window.location.href = '/users';
                })
            }
        },
        created() {
            this.getAllUsers(this.page);
        }
    }
</script>

<style scoped>
    TD { border-bottom: 1px black dashed;}
</style>