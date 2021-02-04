<template>
  <div class="answer">
    <el-container>
      <el-main>
        <div class="header" slot="header">
          <span>{{answerList.length}}个回答</span>
          <div>默认排序
            <i class="el-icon-d-caret"></i>
          </div>
        </div>
        <el-card v-for="(info,index) in answerList" style="padding: 0; width: 44%;margin: 0 auto;padding: 10px">
          <div class="answer-detail">
            <img :src="info.headerURL" v-on:click="userDetail(index)"/>
            <span>{{info.user.userName}}</span>
            <br/>
            <span style="font-size: 12px">已获取{{info.score}}点积分</span>
            <br/><br/>
            <p>{{info.answer.supportNum}}人赞同了此回答</p>
            <div v-html="info.answer.answer" class="user-insert">{{info.answer.answer}}</div>
          </div>
          <br/>
          <div class="answer-footer">
            <el-button size="mini" icon="el-icon-d-caret" @click="supportAnswer(index)">
              <span v-if="!info.is_support">赞同 {{info.answer.supportNum}}</span>
              <span v-else>已赞同</span>
            </el-button>
            <el-button size="mini" icon="el-icon-chat-dot-round" type="text" @click="commentHide(index)">评论</el-button>
            <el-button size="mini" type="text" @click="storeTheAnswer(index)">
              <i class="el-icon-star-off" v-if="!info.isStore"></i>
              <i class="el-icon-star-on" v-else></i>
              <span v-if="!info.isStore">收藏</span>
              <span v-else>已收藏</span>
            </el-button>
            <el-button size="mini" type="text"  @click="giveScore(index)" v-if="myQuestion">评分</el-button>
            <answer-score v-bind:questionID="questionID" v-bind:answerID="info.answer.answer_id" v-if="info.is_score && myQuestion" style="margin-top: 20px" v-bind:maxValue="maxValue" v-on:distriScore="scoreEvent($event, index)"></answer-score>
          </div>
          <comment-answer v-bind:answerID="info.answer.answer_id" v-bind:questionID="questionID" v-if="info.comment"></comment-answer>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
    import ElContainer from "element-ui/packages/container/src/main";
    import ElHeader from "element-ui/packages/header/src/main";
    import answer from '@/util/answer'
    import CommentAnswer from "@/components/comment";
    import user from '@/util/user'
    import log from '@/util/log'
    import AnswerScore from "@/components/answerScore";
    import ElAside from "element-ui/packages/aside/src/main";
    export default {
        name: "Answer",
      components: {ElAside, AnswerScore, CommentAnswer, ElHeader, ElContainer},
      props: ['questionID', 'myQuestion', 'maxValue'],
      data() {
          return {
            answerList: [],
          }
      },

      mounted(){
          this.loadAnswerList()
      },
        methods: {
          scoreEvent: function(score, index){
            this.answerList[index].is_score = false
            this.answerList[index].score += score
            this.$emit('changeScore', score)
          },
          giveScore: function(index){
            this.answerList[index].is_score = !this.answerList[index].is_score
            console.log(this.answerList[index].is_score)
          },
          storeTheAnswer(index){
            console.log(this.answerList[index].isStore)
            this.answerList[index].isStore = !this.answerList[index].isStore
            answer.storeTheAnswer(this.answerList[index].answer.answer_id, this.$store.getters.getUserID, this.answerList[index].isStore.toString())
          },
          supportAnswer: function(index){
            if(this.answerList[index].is_support) {
              this.answerList[index].answer.supportNum -= 1
              this.answerList[index].is_support = false
            }
            else{
              this.answerList[index].answer.supportNum += 1
              this.answerList[index].is_support = true
            }
            answer.support(this.answerList[index].answer.answer_id, this.$store.getters.getUserID, this.answerList[index].is_support)
          },
          commentHide: function(index){
            this.answerList[index].comment = !this.answerList[index].comment
          },
          userDetail: function(index){
            this.$store.commit('setOtherUserID', this.answerList[index].answer.answer_user)
            if(this.answerList[index].answer.answer_user === this.$store.getters.getUserID)
              this.$router.push('/userpage')
            else
              this.$router.push('/otherUserShow')
          },
          loadAnswerList: function(){
            let _this = this
            answer.getAllAnswers(this.$store.getters.getQuestionID, function(res){
              let that = _this
              res.forEach(ans=>{
                user.getUserInfo(ans.answer_user, function(u){
                  answer.getSupport(u.user_id, ans.answer_id, function(sup){
                    answer.isStore(ans.answer_id, that.$store.getters.getUserID, function (store) {
                      log.getPeopleDiscribe(ans.answer_user, function(discribe){
                        answer.getAnswerScore(ans.answer_id, function(score){
                          let info = {
                            answer: null,
                            user: null,
                            comment: false,
                            is_support: false,
                            isStore: false,
                            headerURL: null,
                            is_score: false,
                            score: 0
                          }
                          info.user = u
                          info.answer = ans
                          info.comment = false
                          info.is_support = sup
                          info.isStore = store
                          info.headerURL = discribe.header_picture
                          info.score = score
                          if(info.headerURL === null)
                            info.headerURL = "C:/Users/绣春之刃/WebstormProjects/TrueWebFront/src/assets"
                          console.log(info.headerURL)
                          that.answerList.push(info)
                        })
                      })
                    })
                  })
                })
              })
            })
          }
        }
    }
</script>

<style scoped>
  .header span{
    float: left;
    font-size: 12px;
    font-weight: bolder;

  }
  .header div{
    float: right;
    font-size: 12px;
    font-weight: lighter;
    margin-right: 50px;
  }
  .answer-detail img{
    width: 40px;
    height: 40px;
    float:left;
  }
  .answer-detail span{
    margin-left: 10px;
  }
  .answer-detail p{
    font-size: 12px;
    font-weight: lighter;
  }
  .user-insert p{
    font-size: 12px;
    word-wrap: break-word;
    border: 20px;
  }
  .answer-footer{
    width: 100%;
  }
  .user-insert>img{
    width: 40px;
    height: 40px;
  }
</style>

