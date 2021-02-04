<!--热榜组件-->
<!--传入问题所有信息-->
<template>
  <div class="hotTemp">
    <el-header><Header></Header></el-header>
    <el-card v-for="(info, index) in hotQuestionList" style="width: 570px;margin: 0 auto;position: relative;left: 180px;margin-top: 30px" v-on:dblclick.native="gotoDetail(index)">
		<el-container style="">
        <el-aside><p style="margin: 0 auto">{{index + 1}}</p></el-aside>
        <el-main>
          <div class="main-content" style="position: relative;bottom: 10px;width: 500px;">
            <p>{{info.question}}</p>
            <div class="con">悬赏积分：{{info.integral}}</div>
            <div>
              <i class="el-icon-edit" style="font-size: 12px;float: left">
                写回答
              </i>
              <i class="el-icon-s-promotion" style="margin-left: 30px;font-size: 12px;float: left;"><span>分享</span></i>
            </div>
          </div>
        </el-main>
    </el-container>
    </el-card>
  </div>
</template>

<script>
    import Header from '@/components/header'
    import axios from 'axios'
    import question from '@/util/question'
    import ElHeader from "element-ui/packages/header/src/main";
    import ElContainer from "element-ui/packages/container/src/main";
    export default {
        name: "hotlist",
        components:{
          ElContainer,
          ElHeader,
            Header
        },
        props: {
          questionID: {
            type: String,
            default: ""
          },
          title: {
            type: String,
            default: "测试标题"
          },
          text: {
            type: String,
            default: "测试内容，稍后修改,真正的男人，敢于面对淋漓的鲜血"
          },
          ranking: {
            type: Number,
            default: 1
          },
          hotInformation: {
            type: Number,
            default: 0
          }
        },
        data() {
          return {
            hotQuestionList: []
          }
        },
        mounted: function(){
         this.loadHotQuestionList()
        },
        methods: {
          gotoDetail: function(index){
            let questionID = this.hotQuestionList[index].question_id
            this.$store.commit('setQuestionID', questionID)
            this.$router.push('/problemDetail')
          },
          loadHotQuestionList: function(){
            const _this = this
            question.getHighScoreQuestions(function(questionList){
              _this.hotQuestionList = questionList.slice()
              console.log(_this.hotQuestionList)
            })
          },
          /**
           * 获取问题热榜信息
           */
          getQuestionHotRanking: function (questionID) {

          },
          getPhotoURL: function (questionID) {

          }
        }
    }
</script>

<style scoped>
  .el-container{
    width: 100%;
    height: 100%;
  }
  .el-aside{
    max-width: 20px;
    width: 5%;
    text-align: center;
    padding-top: 3%;
  }
  .el-aside p{

  }
  .el-header{
  }
  .el-main{
    height: 100%;
    padding: 0px;
    margin: 0;
    border: 0;
    margin: 0;
  }
  .main-content p{
    margin: 12px;
    font-weight: bolder;
    font-size: 16px;
  }
  .main-content .con{
    margin: 12px;
    font-size: 12px;
    overflow: hidden;
    word-wrap: break-word;
    height: 35%;
  }
  .main-content div{
    margin: 12px;
  }
  .hotTemp{
    width: 70%;
    height: 100px;
    border-top: 0.1mm solid;
    border-color: #8c939d;
  }
  .main-content{
    padding-top: 3%;
    width: 68%;
    height: 100%;
    float: left;
    padding: 0;
  }
  .main-content p{
    height: 10px;
  }
  .image-content img{
    width: 100%;
    height: 100%;
  }
  .image-content{
    width: 32%;
    float: left;
    height: 100%;
  }
</style>
