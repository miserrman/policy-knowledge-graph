<template>
  <div style="text-align: center">
    <el-card @click.native="policyDetail(index)" v-for="(policy,index) in policyViewList" style="text-align:left;width:700px;margin:0 auto">
      <div class="card-detail">
        <img :src="imgList[index]">
        <span class="policy-title">{{policy.title}}</span>
        <br/>
        <br/>
        <span class="policy-title-info">政策部门：{{policy.depart}}</span>
      <br/>
      <span class="policy-title-info">发布时间：{{policy.date}}</span>
        <!--        <div class="question-detail">-->
        <!--          <span>{{question.hostQuestion.answer}}</span>-->
        <!--        </div>-->
      </div>
      <br/>
      <div class="card-footer">
        <el-button size="mini" icon="el-icon-caret-top" style="margin-top: 10px">政策热度</el-button>
    <el-button icon="el-icon-edit" size="mini" type="text" style="margin-left: 100px" @click="gotoProblemDetail(index)">添加回答</el-button>
    <el-button icon="el-icon-position" size="mini" type="text" style="margin-left: 30px">分享</el-button>
    <el-button icon="el-icon-view" size="mini" type="text" @click="watchQuestion(index)">
      <span>关注</span>
    </el-button>
  </div>
  </el-card>
  <div class="block">
    <el-pagination v-if="policyList.length > pageCount"
            @current-change="pageViewChange"
            layout="prev, pager, next"
            :page-count="pageTotal">
    </el-pagination>
  </div>
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
          imgList: [],
          policyViewList: [],
          isPageSplit: true,
          pageCount: 10,
          pageTotal: 0,
          pageCurrent: 1,
          t: "../../assets/logo.png"
        }
      },
      mounted(){
        this.loadQuestionList()

      },
      methods:{
        policyDetail: function(index) {
          this.$store.commit('setPolicyId', this.policyViewList[index].id)
          this.$router.push("/problemDetail")
        },
        pageViewChange: function(currentPage) {
          this.policyViewList = []
          let start = (currentPage - 1) * this.pageCount
          for (let i = start; i < Math.min(start + this.pageCount, this.policyList.length); i++) {
            this.policyViewList.push(this.policyList[i])
          }
          this.imgList = []
          for (let i = 0; i < this.policyViewList.length; i++) {
              let random = parseInt(Math.random() * 5) + 1
              if (random == 1) {
                console.log("sdcsdcsdcsdc")
                this.imgList.push(require("@/assets/home/1.jpg"))
              }
              else if (random == 2) {
                this.imgList.push(require("@/assets/home/2.jpg"))
              }
              else if (random == 3) {
                this.imgList.push(require("@/assets/home/3.jpg"))
              }
              else if (random == 4) {
                this.imgList.push(require("@/assets/home/4.jpg"))
              }
              else if (random == 5) {
                this.imgList.push(require("@/assets/home/5.jpg"))
              }
            }
        },
        loadQuestionList: function(){
          let _this = this
          axios.get(api.getAllPolicyTitle, {
            params: {
              type: _this.kind
            }
          }).then(function (response) {
            _this.policyList = response.data.data
            
            let t = Math.floor(_this.policyList.length / _this.pageCount)
            if (_this.policyList.length % _this.pageCount > 0) {
              t += 1
            }
            _this.pageTotal = t
            _this.pageViewChange(1)
            console.log(_this.imgList)
          }).catch(function (err) {
            console.log(err)
          })
        },
      }
    }
</script>

<style scoped>
  .policy-title{
    font-weight: bolder;
  }
  .card-detail img{
    width: 25%;
    margin-right: 30px;
    float: left;
    height: 120px;
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
  .policy-title-info {
    margin-top: 30px;
    font-size: 14px;
    color: rgb(160, 163, 166);
  }
</style>
