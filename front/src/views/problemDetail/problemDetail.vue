<template>
  <div style="background-color: rgba(250,250,250,0.9)">
    <Header></Header>
    <el-card style=" width: 60%;margin: 0 auto;margin-top: 90px;padding: 0" >
      <span class="question-title">{{policy.policyTitle.title}}</span>
      <br/><br/>
      <span class="policy-title-info">{{policy.policyTitle.depart}}</span>
      <span>&nbsp;&nbsp;</span>
      <span class="policy-title-info">{{policy.policyTitle.date}}</span>
      <br/><br/>
      <div v-if="policy_tag.length > 0">
      <span class="tag-label">智能标签：</span>
      <el-tag size="small" style="border-radius: 10px;border-top:20px;position:relative;bottom:1px;margin-right:5px" v-for="(tag, index) in policy_tag">{{tag}}</el-tag>
      <br/><br/>
      </div>
      <div>
        <el-button size="mini" @click.native="gotoFollow">
          <span v-if="!isWatch">关注问题</span>
          <span v-else>已关注</span>
        </el-button>
        <el-button size="mini" @click.native="toPolicyContent" icon="el-icon-edit" plain>政策内容</el-button>
        <el-button size="mini" icon="el-icon-user" plain @click.native="toPolicyCondition">智能条件</el-button>
      </div>
      <div v-html="policy_content" v-if="!isCondition" class="policy-content"></div>
      <div v-html="policy_condition" v-else class="policy-content"></div>
      <br/>
      <br/>
      <span style="font-size:16px;font-weight:bolder">相关政策：</span>
      <br/>
      <br/>
      <div class="relabox" v-for="(policy, index) in policyRelevantList" @click="clickRelevantPolicy(index)">
        <span class="relevant">{{policy.title}}</span>
        <br/>
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
  import axios from "axios"
  import api from '@/util/api'
  import user from '@/util/user'
    export default {
        name: "problemDetail",
        components:{UserAnswer, Header},
        mounted() {
          this.getThePolicy()
          this.getRelaventPolicy()
        },
        data(){
          return{
            isCondition: false,
            answerList: [],
            policy: '',
            policy_content: '',
            policy_condition: '',
            policy_tag: [],
            isWatch: false,
            policyRelevantList : []
          }
        },
      methods:{
          clickRelevantPolicy: function(index) {
            this.$store.commit("setPolicyId", this.policyRelevantList[index].id)
            this.$router.go(0)
          },
          getRelaventPolicy: function() {
            let url = api.getRelevantPolicyTitles.toString().replace("{id}", this.$store.getters.getPolicyId)
            let _this = this
            axios.get(url,{}).then(function(response) {
              _this.policyRelevantList = response.data.data         
            })
          },
          gotoFollow:function () {
            this.isWatch = !this.isWatch
            question.watchTheQuestion(this.question.question_id, this.$store.getters.getUserID, this.isWatch)
          },
          toPolicyContent:function () {
              // this.$router.push('/writeAns')
              this.isCondition = false
              console.log("cdscsdcds")
          },
          toPolicyCondition: function() {
            this.isCondition = true
          },
          changeMax: function(val){
            this.maxValue -= val
          },
          getThePolicy: function(){
            let _this = this
            let policyId = this.$store.getters.getPolicyId
            console.log(policyId)
            axios.get(api.getPolicyById + policyId).then(function (response) {
              let policy = response.data.data
              _this.policy = policy
              _this.policy_content = policy.policyContent.content
              let policy_list = _this.policy_content.split('|')
              _this.policy_content = ""
              for (let i = 0; i < policy_list.length; i++)
                _this.policy_content += policy_list[i] + '<br/>'
              let policy_condition = policy.policyContent.condition
              if (policy_condition != null && policy_condition != "") {
                let policy_condition_list = policy_condition.split('|')
                for (let i = 0; i < policy_condition_list.length; i++)
                  _this.policy_condition += '<span>' + (i + 1) + '.  ' + policy_condition_list[i] + '</span><br/>'

              }
              _this.policy_tag = policy.tagList
              console.log("csdcsdcsdcsdcsdc")
              console.log(_this.policy_tag)
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
  .policy-title-info {
    margin-top: 30px;
    font-size: 12px;
    color: rgb(160, 163, 166);
  }
  .card-footer{
    margin-top: 10px;
  }
  .tag-label {
    font-size: 12px;
    color: rgb(160, 163, 166);
  }
  .policy-content {
    margin-top: 30px;
    line-height: 25px;
    font-size: 14px;
  }
  .relevant {
    font-size: 14px;
    color: rgb(49, 49, 219);
  }
  .relabox {
    margin-bottom: 8px;
  }
  .relabox :hover {
    text-decoration: underline;
    cursor: pointer;
  }
</style>
