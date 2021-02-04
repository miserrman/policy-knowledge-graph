<template>
    <div>
        <Header></Header>
        <el-autocomplete style="margin-top:100px;margin-left:400px;width: 400px;" v-model="search" :fetch-suggestions="querySearch" placeholder="请输入你想要进行的查询"
                         :trigger-on-focus="false" @select="handleSelect" @keyup.native="headerSearch">
            <el-button @click.native="gotoSearch" slot="append" icon="el-icon-search"></el-button>
        </el-autocomplete>
        <div style="margin-top:10px;margin: 0 auto;width: 500px;">
            <!-- <el-button style="margin-left:20px;" type="text">相关</el-button> -->
            <a style="font-size:20px; margin-left:20px;color:grey">相关</a>
            <el-button style="margin-left:20px;" type="text" @click.native="showFilter">
                <span>筛选</span>
                <i v-if="isArrowUp" class="el-icon-arrow-up"></i>
                <i v-else class="el-icon-arrow-down"></i>
            </el-button>
            <el-card v-if="!isArrowUp" style="width:500px;">
                <el-form label-width="80px">
                    <el-form-item label="日期">
                        <el-radio-group v-model="filterTime">
                            <el-radio :label="0">全部</el-radio>
                            <el-radio :label="1">一周内</el-radio>
                            <el-radio :label="2">一月内</el-radio>
                            <el-radio :label="3">一年内</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="内容">
                        <el-radio-group v-model='content'>
                            <el-radio :label="0">全部</el-radio>
                            <el-radio :label="1">只看问题</el-radio>
                            <el-radio :label="2">只看答案</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="分类">
                        <el-radio-group v-model='kind'>
                            <el-radio :label="0">全部</el-radio>
                            <el-radio :label="1">科学</el-radio>
                            <el-radio :label="2">数码</el-radio>
                            <el-radio :label="3">体育</el-radio>
                            <el-radio :label="4">时尚</el-radio>
                            <el-radio :label="5">影视</el-radio>
                            <el-radio :label="6">生活</el-radio>
                            <el-radio :label="7">游戏</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="标签">
                        <el-radio-group v-model="tag">
                            <el-radio :label="0">全部</el-radio>
                            <el-radio v-for="(tag,index) in tags" :label="index + 1">{{tag}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </el-card>
            <el-card @dblclick.native="gotoDetail(index)" style="width:600px;margin:5px auto" v-for="(info, index) in questions">
                <el-container>
                    <el-header>
                        <span style="float:right;font-size:12px;">{{info.question.question_time}}</span>
                        <img class="imgpos" style="float:left" :src="info.header_picture">
                        <b style="margin-left:30px">Author: {{info.user.userName}}</b>
                        <span style="margin-left:20px;">悬赏积分：{{info.question.integral}}</span>
                        <br/>
                        <el-tag size="small" style="margin-left:30px;margin-top:10px;" v-for="tag in info.question.label_m">{{tag}}</el-tag>
                    </el-header>
                    <el-main>
                        <span>
                          {{info.question.question}}
                        </span>
                    </el-main>
                </el-container>
            </el-card>
            <el-card  style="width:600px;margin:5px auto" v-for="info in answers">
                <el-container>
                    <el-header>
                        <span style="float:right">{{info.answer.answer_time}}</span>
                        <img class="imgpos" style="float:left" :src="info.header_picture">
                        <b style="margin-left:30px">{{info.user.userName}}</b>
                        <br/>
                        <el-tag size="small" style="margin-left:30px;margin-top:10px;" v-for="tag in searchTags">{{tag}}</el-tag>
                    </el-header>
                    <el-main>
                      <div v-html="info.answer.answer">
                        {{info.answer.answer}}
                      </div>
                    </el-main>
                </el-container>
            </el-card>
            <!-- <el-card v-for="question in questions" >

            </el-card>
            <el-card v-for="answer in answers" >

            </el-card> -->
        </div>
    </div>
</template>

<script>
import Header from '@/components/header'
const kindOptions = ['全部','科学','数码','体育','时尚','影视','生活','游戏']
import search from '@/util/search'
import question from '@/util/question'
import answer from '@/util/answer'
import user from "@/util/user";

export default {
    name:'search',
    components:{
        Header
    },
    data(){
        return{
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
            tempAnswers: []
        }
    },
    mounted(){
        // this.isArrowUp = this.$store.getters.isArrowUp
        // this.content = this.$store.getters.filterContent
        // this.kind = this.$store.getters.filterKind
        // this.tag = this.$store.getters.filterTag
      this.searchList = []
      this.loadSearch()
    },
    methods:{
        loadSearch: function(){
          const _this = this
          this.tempQuestions = []
          this.tempAnswers = []
          this.questions = []
          this.answers = []
          search.getSearchItem(this.$store.getters.getSearchContent, function(searchItemList){
            searchItemList.forEach(search=>{
              if(search.type == 0){
                question.getTheQuestion(search.iD, function(q){
                  user.getUserInfo(q.user_id, function(u){
                    user.getOtherDiscribe(u.user_id, function(discribe){
                      let info = {
                        question: q,
                        user: u,
                        header_picture: ''
                      }
                        let time = new Date(info.question.question_time)
                        info.question.question_time = time.toLocaleString()
                        let str = new String(info.question.label_m)
                        info.question.label_m = str.split(',')
                        info.header_picture = require('../../assets/img.jpg')
                        if(discribe != undefined && discribe.header_picture != null)
                          info.header_picture = discribe.header_picture
                      _this.questions.push(info)
                      _this.tempQuestions.push(info)
                    })
                  })
                 })
              }
              else{
                answer.getAnswer(search.iD, function(ans){
                  question.getTheQuestion(ans.question_id, function(ques){
                    user.getUserInfo(ans.answer_user, function(u){
                      user.getOtherDiscribe(u.user_id, function(discribe){
                        let info = {
                          answer: ans,
                          user: u,
                          header_picture: '',
                          question: ques
                        }
                        info.header_picture = require('../../assets/img.jpg')
                        let time = new Date(info.answer.answer_time)
                        info.answer.answer_time = time.toLocaleString()
                        if(discribe != null)
                          info.header_picture = discribe.header_picture
                        _this.answers.push(info)
                        _this.tempAnswers.push(info)
                      })
                    })
                  })
                })
              }
            })
          })
        },
        showFilter:function(){
            this.isArrowUp = !this.isArrowUp
        },
      handleSelect: function(item){
          this.search = item.value
          this.$store.commit('setSearchContent', item.value)
          this.questions = []
          this.answers = []
          this.loadSearch()
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
      isArrowUp: function (newVal, oldVal) {
        this.$store.commit('isArrowUp', newVal)
      },
      content: function (newVal, oldVal) {
        if (newVal == 1) {
          this.answers = []
          this.questions = this.tempQuestions.slice()
        } else if (newVal == 2) {
          this.questions = []
          this.answers = this.tempAnswers.slice()
        } else {
          this.loadSearch()
        }
      },
      search: function(){
        let _this = this
        search.getSearchItem(this.search, function(searchList){
          _this.searchItemList = searchList
          _this.searchList = []
          searchList.forEach(search=>{
            let info = {
              value: search.content,
              id: ''
            }
            _this.searchList.push(info)
          })
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
.el-button{
    font-size: 20px;
    color: grey;
}
.imgpos{
    position: relative;
    width: 60px;
    height: 60px;
}
</style>

