<template>
  <div style="background-color: rgba(250,250,250,0.9)">
    <Header></Header>
    <el-card style=" width: 43%;margin: 0 auto;margin-top: 90px;padding: 0" >
      <el-tag size="small" style="border-radius: 10px;margin-top: 0px;margin-left: 10px" v-for="tag in question.label_m">{{tag}}</el-tag>
      <br/>
      <span class="question-title">{{question.question}}</span>
      <br/>
      <div class="card-footer">
        <el-button size="mini" @click.native="gotoFollow">
          <span v-if="!isWatch">关注问题</span>
          <span v-else>已关注</span>
        </el-button>
        <el-button size="mini" @click.native="gotoAnswer" icon="el-icon-edit">写回答</el-button>
        <el-button size="mini" icon="el-icon-user">邀请回答</el-button>
      </div>
    </el-card>
    <user-answer v-bind:myQuestion="myQuestion" v-bind:maxValue="maxValue" v-on:changeScore="changeMax($event)"></user-answer>
  </div>
</template>

<script>
  import Header from '@/components/header'
  import answer from '@/util/answer'
  import question from '@/util/question'
  import UserAnswer from '@/components/answer'
  import user from '@/util/user'
    export default {
        name: "problemDetail",
        components:{UserAnswer, Header},
        mounted() {
          this.getTheQuestion()
        },
        data(){
          return{
            answerList: [],
            question: '',
            myQuestion: false,
            maxValue: 0,
            isWatch: false
          }
        },
      methods:{
          gotoFollow:function () {
            this.isWatch = !this.isWatch
            question.watchTheQuestion(this.question.question_id, this.$store.getters.getUserID, this.isWatch)
          },
          gotoAnswer:function () {
              this.$router.push('/writeAns')
          },
          changeMax: function(val){
            this.maxValue -= val
          },
        getTheQuestion: function(){
            let _this = this
            user.isWatchQuestion(this.$store.getters.getQuestionID, this.$store.getters.getUserID, function (isWatch) {
              _this.isWatch = isWatch
            })
            question.getTheQuestion(this.$store.getters.getQuestionID, function(response){
              if(response.label_m === undefined)
                response.label_m = '问题'
              let label = response.label_m.toString()
              let label_arr = label.split(',')
              _this.question = response
              _this.question.label_m = label_arr
              console.log(response)
              if(response.user_id == _this.$store.getters.getUserID)
                _this.myQuestion = true
              _this.maxValue = response.integral
            })
          }
      }
    }
</script>

<style scoped>
.el-tag{
  position: relative;
  bottom: 9px;
}
  .question-title{
    font-size: 18px;
    font-weight: bolder;
  }
  .card-footer{
    margin-top: 10px;
  }
</style>
