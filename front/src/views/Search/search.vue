<template>
    <div>
        <Header></Header>
        <el-autocomplete style="margin-top:100px;margin-left:400px;width: 400px;" v-model="search" :fetch-suggestions="querySearch" placeholder="请输入你想要进行的查询"
                         :trigger-on-focus="false" @select="handleSelect" @keyup.native="headerSearch">
            <el-button @click.native="gotoSearch" slot="append" icon="el-icon-search"></el-button>
        </el-autocomplete>
        <div style="margin-top:10px;margin:0 auto;width: 700px;">
            <!-- <el-button style="margin-left:20px;" type="text">相关</el-button> -->
            <a style="font-size:12px; margin-left:20px;color:grey">相关</a>
            <el-button style="margin-left:20px;" type="text" @click.native="showFilter">
                <span>筛选</span>
                <i v-if="isArrowUp" class="el-icon-arrow-up"></i>
                <i v-else class="el-icon-arrow-down"></i>
            </el-button>
            <div v-if="!isArrowUp" style="width:700px; margin-bottom:30px">
              <el-form :inline="true" class="demo-form-inline">
  <el-form-item label="发布部门">
                    <el-select placeholder="选择层级" v-model="form.depart.level" style="width:100px;margin-right:20px;margin-left:20px" size="mini" @change="changeCountry">
                      <el-option label="国家级" value="0"></el-option>
                      <el-option label="省市级" value="1"></el-option>
                      <el-option label="地区级" value="2"></el-option>
                    </el-select>
                    <el-select :disabled="departDisabled" placeholder="选择地区" size="mini" v-model="form.depart.region" style="width:150px;margin-right:20px" @change="changeRegion">
                      <el-option v-for="(region, index) in regionList" :label="regionList[index]" :value="regionList[index]"></el-option>
                    </el-select>
                    <el-select placeholder="选择部门" size="mini" v-model="form.depart.name" style="width:300px">
                      <el-option v-for="(depart, index) in departList" :label="departList[index]" :value="departList[index]"></el-option>
                    </el-select>
                </el-form-item>
 <el-form-item label="发布时间" style="width:800px">
    <el-date-picker type="date" placeholder="起始日期" size="small" style="width:120px" v-model="form.startTime"></el-date-picker>
    <span>&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;</span>
    <el-date-picker placeholder="结束日期" v-model="form.endTime" size="small"  style="width:130px"></el-date-picker>
 </el-form-item>
              </el-form>
            </div>
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

            <!-- <el-card v-for="question in questions" >

            </el-card>
            <el-card v-for="answer in answers" >

            </el-card> -->
        </div>
    <el-pagination style="text-align:center" v-if="policyList.length> pageCount"
            @current-change="pageViewChange"
            layout="prev, pager, next"
            :page-count="pageTotal">
    </el-pagination>
    </div>
</template>

<script>
import Header from '@/components/header'
import filterPolicy from '@/components/filter'
const kindOptions = ['全部','科学','数码','体育','时尚','影视','生活','游戏']
import search from '@/util/search'
import api from '@/util/api'
import axios from 'axios'
import question from '@/util/question'
import answer from '@/util/answer'
import user from "@/util/user";

export default {
    name:'search',
    components:{
        Header, filterPolicy
    },
    data(){
        return{
            form: {
              depart: {
                level: "",
                region: "",
                name: ""
              },
              startTime: "",
              endTime: ""
            },
            allDepartData: [],
            allRegionData: [],
            departList: [],
            regionList: [],
            pageCount: 10,
            filterTime:0,
            isArrowUp:true,
            content:0,
            kind:0,
            tag:0,
            kinds:kindOptions,
            tags:['js','java','vue'],
            questions:[],
            questionList: [],
            answerList: [],
            answers:[],
            searchTags:['js'],
            score:'20',
            search: '',
            searchList: [],
            tempQuestions: [],
            tempAnswers: [],
            policyList: [],
            policyViewList: [],
            imgList: [],
            departDisabled: false
        }
    },
    mounted(){
        // this.isArrowUp = this.$store.getters.isArrowUp
        // this.content = this.$store.getters.filterContent
        // this.kind = this.$store.getters.filterKind
        // this.tag = this.$store.getters.filterTag
      this.searchList = []
      this.loadSearch()
      this.policyDepartInit()
    },
    methods:{
       changeCountry: function() {
            this.departList = []
            if (this.form.depart.level == 0) {
              this.departDisabled = true
              for (let i = 0; i < this.allDepartData.length; i++) {
                if (this.allDepartData[i].level == 0)
                  this.departList.push(this.allDepartData[i].name)
              }
            }
            else if (this.form.depart.level == 1) {
              this.regionList = []
              this.departDisabled = false
              for (let i = 0; i < this.allRegionData.length; i++) {
                this.regionList = this.allRegionData
              }
              this.regionList.push("广东省")
            }
          },
          changeRegion: function() {
            this.departList = []
            for (let i = 0; i < this.allDepartData.length; i++) {
              if (this.allDepartData[i].region == this.form.depart.region)
                this.departList.push(this.allDepartData[i].name)
            }
            console.log(this.allDepartData)
          },
        policyDepartInit: function() {
          let _this = this
          axios.get(api.getPolicyCreateInfo, {}).then(function(response) {
              let data = response.data.data
              _this.allDepartData = data.departs
              _this.allRegionData = data.regions             
          })
        },
        policyDetail: function(index) {
          this.$store.commit('setPolicyId', this.policyList[index].id)
          this.$router.push("/problemDetail")
        },
        loadSearch: function(){
          const _this = this
          let departId = null
          if (this.form.depart.name != "") {
            for (let i = 0; i < this.departList.length; i++) {
              if (this.departList[i] == this.form.depart.name) {
                departId = this.departList[i].id
              }
            }
          }
          let startDate = null
          if (this.form.startTime != "")
            startDate = this.form.startTime
          let endDate = null
          if (this.form.endTime != "")
            endDate = this.form.endTime
          this.search = _this.$store.getters.getSearchContent
          console.log(startDate)
          axios.get(api.searchPolicyByTitle, {
            params: {
              title: _this.$store.getters.getSearchContent,
              departId: departId,
              startDate: startDate,
              endDate: endDate
            }
          }).then(function(response) {
            _this.policyList = response.data
            let t = Math.floor(_this.policyList.length / _this.pageCount)
            if (_this.policyList.length % _this.pageCount > 0) {
              t += 1
            }
            _this.pageTotal = t
            _this.pageViewChange(1)
          })
        },
        showFilter:function(){
            this.isArrowUp = !this.isArrowUp
        },
      handleSelect: function(item){
          this.$store.commit('setPolicyId', item.Id)
          this.$router.push("/problemDetail")
      },
      querySearch(queryString, cb){
        var searchList = this.searchList
        var results = queryString ? searchList.filter(this.createFilter(queryString)):searchList
        clearTimeout(this.timeout)
        this.timeout = setTimeout(()=>{
          cb(searchList)
        },1000 * Math.random());
      },
      createFilter(queryString){
        return (search)=>{
          return (search.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1)
        }
      },
      headerSearch: function(){
        // let _this = this
        // search.getSearchItem(this.search, function(searchList){
        //   _this.searchItemList = searchList
        //   _this.searchList = []
        //   searchList.forEach(search=>{
        //     let info = {
        //       value: search.content,
        //       id: ''
        //     }
        //     _this.searchList.push(info)
        //   })
        // })
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
      gotoDetail:function(index){
        this.$store.commit('setQuestionID', this.questions[index].question.question_id)
        this.$router.push('/problemDetail')
      },
      gotoSearch: function () {

        // this.$message({
        //     showClose:true,
        //     message:'该功能暂未开放',
        //     type:'warning'
        // })
        if (this.search) {
          this.$store.commit('setSearchContent', this.search)
          this.loadSearch()
        } else {
          this.$message({
            showClose: true,
            message: '请输入想要查询的内容',
            type: 'warning'
          })
        }
      }
    },
    watch: {
      search: function(){
        let _this = this
        _this.searchList = []
        axios.get(api.searchPolicyByTitle, {
            params: {
                title: _this.search
            }
        }).then(function(response) {
            let policies = response.data

            for (let i = 0; i < policies.length; i++) {
                let search = {
                    "Id": policies[i].id,
                    "value": policies[i].title
                }
                _this.searchList.push(search)
            }
        }).catch(function (err) {

        })
      },
      kind: function (newVal, oldVal) {
        this.questions = this.tempQuestions.slice()
        this.answers = this.tempAnswers.slice()
        let value = ''
        switch (newVal) {
          case 0:
            value = '全部';
            break;
          case 1:
            value = '科学';
            break;
          case 2:
            value = '数码';
            break;
          case 3:
            value = '体育';
            break;
          case 4:
            value = '时尚';
            break;
          case 5:
            value = '影视';
            break;
          case 6:
            value = '生活';
            break;
          case 7:
            value = '游戏';
            break;
        }
        if (value == '全部') {
          return
        } else {
          this.answers = this.answers.filter((ans) => {
           if(ans.question.complement == undefined)
             return false
            let str = ans.question.complement
            let s = new String(str)
            let res = s.search(value)
            console.log(res)
            return res == 0
          })
          this.questions = this.questions.filter(function (ques) {
            if (ques.question.complement == undefined)
              return false
            let str = ques.question.complement
            let s = new String(str)
            let res = s.search(value)
            return res == 0
          })
        }
      },
      tag: function (newVal, oldVal) {

      },

    }
}
</script>

<style scoped>
.policy-title-info {
    margin-top: 30px;
    font-size: 14px;
    color: rgb(160, 163, 166);
  }
.el-button{
    font-size: 12px;
    color: grey;
}
.imgpos{
    position: relative;
    width: 60px;
    height: 60px;
}
  .card-detail img{
    width: 25%;
    margin-right: 30px;
    float: left;
    height: 100px;
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
   .policy-title{
    font-weight: bolder;
  }
</style>

