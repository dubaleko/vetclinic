<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn text  v-on="on">{{action}}</v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="headline" v-text="action"/>
            </v-card-title>
            <v-card-text>
                <h5 class="validationError" v-if="!$v.article.required && $v.article.$dirty">
                    Информация о приеме не может быть пустой</h5>
                <v-textarea v-model="article" label="Информация о приеме"/>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="save">Сохранить</v-btn>
                <v-btn color="blue darken-1" text @click="dialog=false">Закрыть</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "VisitHistoryDialog",
        props:['user','employee','date','action','visit','orderId'],
        data(){
            return{
                dialog:false, article: null, myVisit: null
            }
        },
        updated() {
          if(this.visit != this.myVisit){
              this.article = this.visit.article;
              this.myVisit = this.visit;
          }
        },
        validations:{
            article: {required}
        },
        methods:{
            save(){
                this.$v.$touch();
                if (this.$v.$invalid){
                    return
                }
                else{
                    if (this.action == 'Добавить запись'){
                        let visit = {id: null, employee: this.employee, user: this.user,date: this.date,article: this.article}
                        this.$http.post('/api/visitHistory/?orderId='+this.orderId,visit).then(function (response) {
                            window.location.href = "/talons"
                        })
                    }
                    else {
                        this.visit.article = this.article;
                        this.$http.put('/api/visitHistory',this.visit).then(function (response) {
                            window.location.href = "/visitLog"
                        })
                    }
                }
            }
        }
    }
</script>

<style scoped>
    .validationError{color: red;}
</style>