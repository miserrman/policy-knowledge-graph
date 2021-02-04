<template>
  <div style="text-align: center">
    <el-card @dblclick.native="gotoProblemDetail(index)" v-for="(policy,index) in policyList" style="text-align:left;width:600px;margin:0 auto">
      <div class="card-detail">
        <span>{{policy.title}}</span>
        <br/>
        <img src="../../assets/logo.jpg"/>
<!--        <div class="question-detail">-->
<!--          <span>{{question.hostQuestion.answer}}</span>-->
<!--        </div>-->
      </div>
      <br/>
      <div class="card-footer">
        <el-button size="mini" icon="el-icon-caret-top" style="margin-top: 10px">回答数</el-button>
        <el-button icon="el-icon-edit" size="mini" type="text" style="margin-left: 100px" @click="gotoProblemDetail(index)">添加回答</el-button>
        <el-button icon="el-icon-position" size="mini" type="text" style="margin-left: 30px">分享</el-button>
        <el-button icon="el-icon-view" size="mini" type="text" @click="watchQuestion(index)">
          <span>关注</span>
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
  import api from '@/util/api'
  import axios from 'axios'
    export default {
        name: "recommend",
        props: ['kind'],
      data(){
        return{
          policyList: [],
        }
      },
      mounted(){
        this.loadQuestionList()
        console.log(this.kind)
      },
      methods:{
        loadQuestionList: function(){
          console.log("cdscsdcsdc")
          let _this = this
          axios.get(api.getAllPolicyTitle, {
            params: {
              type: _this.kind
            }
          }).then(function (response) {
            _this.policyList = response.data.data

          }).catch(function (err) {
            console.log(err)
          })
        },
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
