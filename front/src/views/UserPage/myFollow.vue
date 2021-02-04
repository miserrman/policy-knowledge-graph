<template>
    <div>
        <el-card @dblclick.native="gotoDetail(index)" v-for="(question, index) in questionList" style="margin-top:30px;margin-left:100px;width:600px">
            <el-container>
                <el-header>
                    <img style="float:left;width:60px;height:60px;" :src="question.header_picture">
                     <b style="float:left;margin-left:40px;">{{question.user.userName}}</b>
                    <span style="float:right;font-size:12px;">{{question.question.question_time}}</span>
                    <br>
                    <span style="float:right;font-size:12px;">悬赏积分：{{question.question.integral}}</span>
                </el-header>
                <el-main>
                     <span>{{question.question.question}}</span>
                </el-main>
                <el-footer>
                    <el-button style="float:right" @click="delQuestion(index)" size="mini" type="danger"  >取消关注</el-button>
                </el-footer>
            </el-container>
        </el-card>
    </div>
</template>

<script>
  import user from '@/util/user'
  import question from '@/util/question'
export default {
    name:'myFollow',
    mounted(){
      this.loadFollowedQuestion()
    },
    data(){
        return{
            questionList:[]
        }
    },
    methods:{
        loadFollowedQuestion: function(){
          const _this = this
          question.getFollowedQuestion(this.$store.getters.getUserID, function(questions){
            console.log(questions)
            questions.forEach(ques=>{
              user.getUserInfo(_this.$store.getters.getUserID, function(u){
                user.getOtherDiscribe(u.user_id, function(describe){
                  let info = {
                    question: ques,
                    user: u,
                    header_picture: ''
                  }
                  let time = new Date(info.question.question_time)
                  info.question.question_time = time.toLocaleString()
                  if(describe == undefined)
                    info.header_picture = ''
                  else {
                    info.header_picture = describe.header_picture
                  }
                  _this.questionList.push(info)
                })
              })
            })
          })
        },
        gotoDetail:function(index){
          this.$store.commit('setQuestionID', this.questionList[index].question.question_id)
          this.$router.push('/problemDetail')
        },
        // 取消关注
        delQuestion:function(index){
          question.watchTheQuestion(this.questionList[index].question.question_id, this.$store.getters.getUserID, false)
          this.questionList.splice(index, 1)
        }
    }
}
</script>
