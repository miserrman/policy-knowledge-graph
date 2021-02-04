<template>
  <el-container style="width: 100%;">
    <el-header>
      <Header></Header>
    </el-header>
    <el-main style="margin: 0 auto">
      <el-card style="width: 100%;text-align: left;margin-top: 10px;float: left">
        <div class="user-info">
          <img :src="user_header_src"/>
          <div>
            <span>{{userName}}</span>
            <div>{{userDiscribe}}</div>
            <br/><br/>
            <el-button type="text" @click="likeUser" style="margin-left: 30px">
              <i class="el-icon-star-off" v-if="!follow">喜欢</i>
              <i class="el-icon-star-on" v-else>已跟随</i>
            </el-button>
          </div>
        </div>
      </el-card>
      <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
      <other-user-guide class="guide"></other-user-guide>
      <user-score v-bind:userID="this.$store.getters.getUserID" v-bind:title="title" style="position: absolute;bottom: 100px;left: 800px;width: 140px;"></user-score>
    </el-main>

  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import ElContainer from "element-ui/packages/container/src/main";
  import ElHeader from "element-ui/packages/header/src/main";
  import user from '@/util/user'
  import UserScore from './UserScore'
  import log from '@/util/log'
  import ElTabPane from "element-ui/packages/tabs/src/tab-pane";
  import OtherUserGuide from "@/views/OtherUser/otherUserGuide";
    export default {
        name: "OtheUserShow",
      components: {OtherUserGuide, ElTabPane, ElHeader, ElContainer, Header, UserScore},
      props: ['userID'],
      data(){
        return{
          userName: '',
          userDiscribe: '像我这样优秀的人，本该灿烂过一生，怎么20多年到头来，还在人海里浮沉',
          user_header_src: '',
          title: '他的',
          activeName: 'second',
          follow: false
        }
      },
      mounted() {
          this.loadUserInfo()
      },
      methods: {
          likeUser: function(){
            this.follow = !this.follow
          },
        handleClick: function(tab, Event){

        },
        loadUserInfo: function () {
          let _this = this
          user.getUserInfo(this.$store.getters.getOtherUserID, function(user){
            _this.userName = user.userName
            log.getPeopleDiscribe(user.user_id, function(dis){
              console.log(dis)
              _this.user_header_src = dis.header_picture
              if(dis.describe)
                _this.userDiscibe = dis.describe
            })
          })
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
  .user-info div span{
    font-size: 24px;
    font-weight: bolder;
    display: block;
    margin: 15px;
  }
  .user-info div div{
    font-size: 12px;
    margin-left: 20px;
    word-wrap: break-word;
    width: 80%;
  }
  .user-info div{
    width: 70%;
  }
  .guide{
    position: relative;

  }
</style>
