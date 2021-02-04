<template>
    <div>
      <el-card v-for="(info,index) in answerList" style="padding: 0;width: 50%;text-align: left;margin: 40px; padding: 10px" >
        <div slot="header">
          <el-button style="float: right;position: relative;bottom: 15px" @click="cancelStore(index)" size="mini" type="danger">取消收藏</el-button>
        </div>
        <div class="answer-detail">
          <img :src="info.headerURL" v-on:click="userDetail(index)"/>
          <span>{{info.user.userName}}</span>
          <br/><br/>
          <p>{{info.answer.supportNum}}人赞同了此回答</p>
          <div v-html="info.answer.answer" class="user-insert">{{info.answer.answer}}</div>
        </div>
        <br/>
      </el-card>
    </div>
</template>

<script>
  import answer from '@/util/answer'
  import log from '@/util/log'
  import user from '@/util/user'
export default {
    name:'myCollect',
    data(){
      return{
        answerList: []
      }
    },
    mounted(){
      this.loadCollectList()
      },
    methods: {
      cancelStore: function(index){
        answer.storeTheAnswer(this.answerList[index].answer.answer_id, this.$store.getters.getUserID, false)
        this.answerList.splice(index, 1)
      },
      loadCollectList: function(){
        const _this = this
        answer.getCollectList(this.$store.getters.getUserID, function(answers){
          answers.forEach(ans=>{
            user.getUserInfo(ans.answer_user, function(u){
              log.getPeopleDiscribe(ans.answer_user, function(d){
                let info = {
                  answer: ans,
                  user: u,
                  headerURL: d.header_picture
                }
                _this.answerList.push(info)
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
  }
</style>
