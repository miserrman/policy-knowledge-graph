<template>
    <el-container>
        <el-card style="width: 60%;text-align: left;margin-top: 10px">
          <div class="user-info">
            <img src="../../assets/img.jpg" style="margin-bottom: 20px;">
            <div style="margin-left:20px">
              <span class="user-name">{{userName}}</span>
              <br/>
              <br/>
              <br/>
              <span class="user-info">邮箱：{{email}}</span>
              <br/>
              <span class="user-info">电话：{{tel}}</span>
              <!-- <div v-if="!isEditDescribe">{{userDiscribe}}</div>
              <div v-else>
                <el-input v-model="describe" type="textarea"></el-input>
              </div> -->
            </div>
          </div>
        </el-card>
        <br/>
        <br/>
        <br/>
      <!-- <el-main>
        <el-tabs style="margin-top:100px;width:60%" v-model="activeName">
          <el-tab-pane label="成就系统" name="first">
            <el-card style="width:30%">
              <!-- <div>
                <i class="el-icon-thumb"></i>
                <span>获得{{approveCount}}次赞同</span>
              </div>
              <div>
                <i class="el-icon-view"></i>
                <span>关注了{{starQuestion}}问题</span>
              </div>
              <div>
                <i class="el-icon-star-on"></i>
                <span>收藏了{{answerCount}}回答</span>
              </div>
              <div>
                <i class="el-icon-question"></i>
                <span>提问了{{askCount}}问题</span>
              </div> -->
            <!-- </el-card>
          </el-tab-pane>
          <el-tab-pane label="签到系统" name="second">
            <el-button @click.native="signIn" type="primary" round>
              <span v-if="!isSign">点我签到</span>
              <span v-else>已签到</span>
            </el-button>
          </el-tab-pane>
        </el-tabs>
      </el-main> -->
    </el-container>
</template>

<script>
    import ElContainer from "element-ui/packages/container/src/main";
    import ElHeader from "element-ui/packages/header/src/main";
    import Header from "@/components/header";
    import user from '@/util/user'
    import log from '@/util/log'
    export default {
        name: "hostPage",
      components: {Header, ElHeader, ElContainer},
      data(){
          return{
            activeName:'first',
            starQuestion:'20',
            approveCount:'30',
            answerCount:'20',
            askCount:'20',
            userName: this.$store.getters.getUserInfo.userName || localStorage.getItem("userName"),
            email: this.$store.getters.getUserInfo.email || localStorage.getItem("email"),
            tel: this.$store.getters.getUserInfo.tele || localStorage.getItem("tele"),
            userDiscribe: '',
            user_header_src: '',
            isEditDescribe: false,
            describe: '',
            isSign: this.$store.getters.getIsSign
          }
      },
      mounted() {
        
        //   this.userName = this.$store.getters.getUserName
        //   if(this.$store.getters.getUserDiscribe != "undefined")
        //     this.userDiscribe = this.$store.getters.getUserDiscribe
        //   this.loadAchieve()
        // this.user_header_src = this.$store.getters.getHeaderPictureURL
        // console.log(this.user_header_src)
        // if(this.user_header_src == 'undefined')
        //   this.user_header_src = require('../../assets/default_header.jpg')
      },
      methods: {
          loadUserInfo: function () {
            let _this = this
            user.getUserInfo(this.$store.getters.getUserName, function (userInfo) {
              _this.userDiscribe = userInfo.describe
            })
          },
          loadAchieve: function(){
            let _this = this
            log.getAchieve(this.$store.getters.getUserID, function(achieve){
              console.log(achieve)
              _this.askCount = achieve.questionNum
              _this.answerCount = achieve.storeAnswerNum
              _this.starQuestion = achieve.followQuestions
              _this.approveCount = achieve.supportNum
            })
          },
        editDescribe: function(){
          if(this.isEditDescribe){
            this.userDiscribe = this.describe
            this.$store.commit('setUserDiscribe', this.describe)
            user.exmainePeopleInformation(this.$store.getters.getUserID, this.$store.getters.getUserTel, this.$store.getters.getHeaderPictureURL, this.describe)
            this.describe = ''
          }
            this.isEditDescribe = !this.isEditDescribe
        },
        signIn:function(){
          if(!this.isSign){
            this.isSign = true
            this.$store.commit('setIsSign', true)
            this.$message({
              showClose:true,
              message:'签到成功，+5积分',
              type:'success'
            })
            log.signOn(this.$store.getters.getUserID)
            let number = new Number(this.$store.getters.getUserScore)
            this.$store.commit('setUserScore', number + 5)
          }
          else{
            this.$message({
              showClose:true,
              message:'一天只能签到一次',
              type:'warning'
            })
          }

        }
      }
    }
</script>

<style scoped>
.user-info img{
  float: left;
  width: 120px;
  height: 120px;
}
  .user-info div{
    float: left;
  }
 .user-info {
   font-size: 14px;
 }
 .user-name {
   font-size: 25px;
   font-weight: bolder;
 }
  .user-info div div{
    font-size: 12px;
    margin-left: 20px;
    word-wrap: break-word;
    width: 60%;
  }
  .user-info div{
    width: 70%;
  }

</style>
