<template>
    <v-container fill-height v-if="user == null || user.role == 'USER'">
        <not-found/>
    </v-container>
    <v-container v-else>
        <h1>Журнал амбулаторного лечения животных</h1>
        <v-expansion-panels>
            <v-expansion-panel class="mb-4"  v-for="visit in visits" :key="visit.id">
                <v-expansion-panel-header>
                    <div v-if="user.role == 'ADMIN'">{{visit.employee.clinic.name}}</div>
                    {{visit.employee.name}}<v-spacer/>
                    {{visit.user.userName}}<v-spacer/>
                    {{visit.dateFormatted}}
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                    <visit-history-dialog :visit="visit" action="Обновить"/>
                    <v-btn text @click="deleteVisit(visit.id)">Удалить</v-btn>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-expansion-panels>
        <v-row align="center" justify="center" v-if="emptyVisits">
            Журнал амбулаторного лечения животных на данный момент пуст
        </v-row>
        <v-pagination v-if="totalVisitPages > 1" @input="getAllVisit(this.visitPage)" v-model="visitPage"
                      :length="totalVisitPages" :total-visible="7" prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
    </v-container>
</template>

<script>
    import NotFound from "./NotFound.vue";
    import {formatDate} from "../methods.js";
    import VisitHistoryDialog from "../components/VisitHistoryDialog.vue";
    export default {
        name: "VisitLog",
        components: {VisitHistoryDialog, NotFound},
        data(){
            return{
                user: null,visits:[], emptyVisits: false, visitPage : null, totalVisitPages: null
            }
        },
        methods:{
            deleteVisit(id){
              this.$http.delete('/api/visitHistory?id='+id).then(function (response) {
                  window.location.href = 'visitLog';
              })
            },
            getAllVisit(page){
                if (!page)
                    page = 1;
                this.$http.get('/api/visitHistory?id='+this.user.id+'&page='+page).then(function (response) {
                    this.visits = response.body.pageList;
                    this.totalVisitPages =  response.body.pageCount;
                    this.visitPage = response.body.page+1;
                    this.emptyVisits = false;
                    if (this.visits.length < 1){
                        this.emptyVisits = true;
                    }
                    this.visits.forEach(element=>{
                        element.dateFormatted = formatDate(element.date);
                    })
                })
                console.log(this.visits);
            },
        },
        created() {
            this.$http.get('/api/users/current').then(function (response) {
                if (response.data.userName != null){
                    this.user = response.data;
                }
                this.getAllVisit(this.visitPage);
            })
        }
    }
</script>

<style scoped>

</style>