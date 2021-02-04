<template>
  <div>
    <div class="score-detail">
      <el-card v-for="(info, index) in questionList" style="text-align: left;padding: 0;width: 430px;margin-bottom: 20px">
        <div class="question-detail">
          <span>{{info.question.question_time}}</span>
          <p>{{info.question.question}}</p>
          <div>可悬赏积分：{{info.question.integral}}</div>
          <br/>
        </div>

      </el-card>
    </div>
  </div>
</template>

<script>
  import question from "@/util/question";

  export default {
    name: "ScoreDistribute",
    data(){
      return{
        questionNum: 0,
        totalScore: 0,
        questionList: [],
        num: 0
      }
    },
    methods: {
      giveScore: function(index, answer_index){
        let b = this.questionList[index].answers[answer_index].isGiveScore
        this.questionList[index].answers[answer_index].isGiveScore = !b
      },
      sureDistribute: function(index, answer_index){
        let b = this.questionList[index].answers[answer_index].isGiveScore
        this.questionList[index].answers[answer_index].isGiveScore = !b
        this.questionList[index].question.integral -= this.num
      },

      loadQuestionList: function(){
        const _this = this
        question.scoreQuestion(this.$store.getters.getOtherUserID, function(scoreDistri){
          _this.questionNum = scoreDistri.length
          scoreDistri.forEach(data=>{
            let info = {
              question: data.questions,
              answers: [],
              isViewAnswer: false
            }
            data.answers.forEach(answer=>{
              let ans = {
                answer: answer.answer,
                user: null,
                isGiveScore: false
              }
              info.answers.push(ans)
            })
            _this.totalScore += info.question.integral
            const date = new Date()
            date.setTime(info.question.question_time)
            info.question.question_time = question.questionTimeCal(date)
            _this.questionList.push(info)
          })
        })
      }
    },
    mounted() {
      this.loadQuestionList()
    }
  }
</script>

<style scoped>
  .aside-window{
    width: 20%;
    float: left;
    margin-left: 30px;
  }
  .score-detail{
    float: left;
    width: 60%;
  }
  .question-detail span{
    font-size: 12px;
    color: #99a9bf;
    float: right;
    display: block;
    margin-left: 20%;
  }
  .question-detail p{
    font-weight: bolder;
    position: relative;
    bottom: 20px;
    display: block;
    float: left;
  }
  .question-detail div{
    float: left;
    font-size: 12px;
    margin-left: 20px;
  }
  .answer-detail{
    width: 100%;
    position: relative;
    right: 100px;
    margin-top: 20px;

  }
  .user-info img{
    width: 40px;
    height: 40px;
    float: left;
  }
  .user-info span{
    font-size: 14px;
    float: left;
    margin-left: 10px;
    font-weight: bolder;
  }
  .distri{
    height: 40px;
    margin-left: 100px;
  }
</style>
