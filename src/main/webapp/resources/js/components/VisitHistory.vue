<template>
    <v-card flat>
        <v-card-title><h3>История посещений</h3></v-card-title>
        <v-card-text>
            <div class="mb-4" v-if="order" >
                <visit-history-dialog action="Добавить запись" :order-id="order.id" :user="order.user"
                                  :employee="order.employeeDto" :date="order.receptionDateDto.date"/>
            </div>
            <v-expansion-panels>
                <v-expansion-panel class="mb-4"  v-for="visit in visits" :key="visit.id">
                    <v-expansion-panel-header>
                        {{visit.employee.clinic.name}}<v-spacer/>
                        {{visit.employee.name}}<v-spacer/>
                        {{visit.dateFormatted}}
                    </v-expansion-panel-header>
                    <v-expansion-panel-content>{{visit.article}}</v-expansion-panel-content>
                </v-expansion-panel>
            </v-expansion-panels>
            <v-row align="center" justify="center" v-if="emptyVisits">
                История посещений на данный момент пуста
            </v-row>
            <v-pagination v-if="totalVisitPages > 1" @input="getAllVisit(this.visitPage)" v-model="visitPage"
                          :length="totalVisitPages" :total-visible="7" prev-icon="arrow_back" next-icon="arrow_forward"></v-pagination>
        </v-card-text>
    </v-card>
</template>

<script>
    import {formatDate} from "../methods.js";
    import VisitHistoryDialog from "./VisitHistoryDialog.vue";
    export default {
        name: "VisitHistory",
        components: {VisitHistoryDialog},
        props: ['user','order'],
        data() {
            return {
                visits:[], emptyVisits: false, visitPage : null, totalVisitPages: null,
            }
        },
        methods:{
            getAllVisit(page){
                if (!page)
                    page = 1;
                this.$http.get('/api/visitHistory/profile?id='+this.user.id+'&page='+page).then(function (response) {
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
            },
        },
        created() {
            this.getAllVisit(this.visitPage);
        }
    }
</script>

<style scoped>

</style>