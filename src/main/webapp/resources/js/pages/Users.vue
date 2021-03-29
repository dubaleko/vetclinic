<template>
    <v-container fill-height v-if="user == null || user.role != 'ADMIN'">
        <v-row justify="center">
            <h4>Извините, нам не удалось найти эту страницу, но мы бросим все силы что бы найти её</h4>
        </v-row>
    </v-container>
    <v-container v-else>
        <h1>Пользователи</h1>
        <table width="100%">
            <tr class="bottom-border" v-for="user in users" :key="user.Name">
                <td align="left">{{user.userName}}</td>
                <td align="right">{{user.role}}</td>
            </tr>
        </table>
        <v-pagination v-if="totalPages > 1" @input="getAllUsers" v-model="page" :length="totalPages" :total-visible="7"
                      prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
    </v-container>
</template>

<script>
    export default {
        name: "Users",
        props:['user'],
        data(){
            return {
                page : null, totalPages: null, users: []
            }
        },
        methods:{
            getAllUsers(page){
                if(!page)
                    page = 1;
                let url = '/api/users?page='+page;
                this.$http.get(url).then(function (response) {
                    this.users = response.data.content;
                    this.totalPages = response.data.totalPages;
                    this.page = response.data.pageable.pageNumber+1;
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