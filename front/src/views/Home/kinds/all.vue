<template>
    <div style="text-align: center">
<!--        <el-card @dblclick.native="gotoProblemDetail(index)" v-for="(question,index) in questionList" style="text-align:left;width:600px;margin:0 auto">-->
<!--          <div class="card-detail">-->
<!--            <span>{{question.question.question}}</span>-->
<!--            <br/>-->
<!--            <img src="@/assets/img.jpg"/>-->
<!--            <div class="question-detail">-->
<!--              <span v-html="question.question.suggestion">{{question.question.suggestion}}</span>-->
<!--            </div>-->
<!--          </div>-->
<!--          <br/>-->
<!--          <div class="card-footer">-->
<!--            <el-button size="mini" icon="el-icon-caret-top" style="margin-top: 10px">回答数{{question.question.answer_num}}</el-button>-->
<!--            <el-button icon="el-icon-edit" size="mini" type="text" style="margin-left: 100px">添加评论</el-button>-->
<!--            <el-button icon="el-icon-position" size="mini" type="text" style="margin-left: 30px">分享</el-button>-->
<!--            <el-button icon="el-icon-view" size="mini" type="text" @click="watchQuestion(index)">-->
<!--              <span v-if="!question.isWatch">关注</span>-->
<!--              <span v-else>已关注</span>-->
<!--            </el-button>-->
<!--          </div>-->
<!--        </el-card>-->
      <recommend v-bind:kind="kind"></recommend>
    </div>
</template>

<script>
  import question from '@/util/question'
  import ElFooter from "element-ui/packages/footer/src/main";
  import Recommend from "@/views/Home/recommend";
export default {
  name:'economy',
  components: {Recommend, ElFooter},
  data(){
        return{
              questionList: [],
              kind: '0'
            }
        },
     mounted(){
        this.loadQuestionList()
    },
    methods:{
      watchQuestion: function(index){
        this.questionList[index].isWatch = !this.questionList[index].isWatch
        let jud = this.questionList[index].isWatch
        question.watchTheQuestion(this.questionList[index].question.question_id, this.$store.getters.getUserID, jud)
      },
      async loadQuestionList(){
         await question.getAllQuestion(this.questionList, this.$store.getters.getUserID)
         console.log(this.questionList)
      },
      gotoProblemDetail:function (index) {
        let id = this.questionList[index].question.question_id
        let que = this.questionList[index].question.question
        this.$store.commit('setQuestionID', id)
        this.$store.commit('setQuestion', que)
        this.$router.push('/problemDetail')
      }
    }
}
</script>
<style scoped>
  .card-detail span{
    font-weight: bolder;
  }
  .card-detail img{
    width: 25%;
    margin-top: 10px;
    float: left;
    height: 100px;
  }
  .question-detail span{
    font-size: 13px;
  }
  .question-detail {
    width: 70%;
    float: left;
    margin-left: 10px;
    padding-top: 10px;
  }
  .card-footer{
    margin-bottom: 10px;
    margin-left: 10px;
    width: 600px;
    float: left;
  }
  .el-footer .el-button{
    float: right;
  }
  </style>
