<template>
    <el-card style="width: 25%;float: right;margin-top: 10px">
      <div class="champion-detail">
        <span>{{title}}积分:{{score}} </span>
        <span>{{title}}回答数: {{answerNum}}</span>
        <span>{{title}}问题数：{{questionNum}}</span>
        <span>他喜欢<i class="el-icon-lollipop"></i></span>
        <div>

        </div>
      </div>
      <span></span>
    </el-card>
</template>

<script>
  //积分暂时用fllowerNum顶替
    import user from "@/util/user";
  import ElCard from "element-ui/packages/card/src/main";

    export default {
        name: "UserScore",
      components: {ElCard},
      props: ['title', 'userID'],
        data(){
          return{
            answerNum: 0,
            questionNum: 0,
            score: 0
          }
        },
      methods: {
        loadUserInfo: function(){
          let _this = this
          user.getUserChampion(this.userID, function(champion){
            _this.score = champion.score
            _this.answerNum = champion.answerNum
            _this.questionNum = champion.questionNum
          })
        }
      },
      mounted(){
        this.loadUserInfo()
      },
    }
</script>

<style scoped>
.champion-detail span{
  font-size: 14px;
  margin-bottom: 10px;
  display: block;
}
</style>
