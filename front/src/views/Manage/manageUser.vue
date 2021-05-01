<template>
  <div>
    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input
        v-model="searchStr"
        placeholder="默认展示部分用户，可以通过用户名搜索用户..."
        prefix-icon="el-icon-search"
         style="width: 400px" size="small">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="">搜索
      </el-button>
    </div>

    <div style="display: flex;justify-content: space-around;flex-wrap: wrap">
      <el-card style="width:330px;margin-top: 10px;" v-for="(user, index) in users"
      >
        <div slot="header" style="text-align: left">
          <el-button style="float: right; padding: 3px 0;color: #ff0509" type="text" icon="el-icon-delete"
                     @click="deleteUser(index)">删除
          </el-button>
        </div>
        <div>
          <div><img :src="imageJudge(index)" style="width: 70px;height: 70px"></div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>用户名:</span><span>{{user.userName}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>电子邮箱:</span><span>{{user.email}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>联系电话:</span><span>{{user.tele}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>用户角色:</span>
              <span v-if="user.role==1">管理员</span>
              <span v-else>用户</span>
            </span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import api from '@/util/api'

  export default {
    name: "manageUser",
    data(){
      return{
        users: [],
        searchStr:''
      }
    },
    mounted() {
     this.loadUserList()
    },
    methods: {
      loadUserList: function(){
        const _this = this
        axios.get(api.getUserList, {
          headers: {
            'user-info': _this.$store.getters.getUserInfo.role || localStorage.getItem("role")
          }
        }).then(function(response) {
          _this.users = response.data.data
          console.log(_this.users)
        })
      },
      imageJudge: function(index){
        const p = this.users[index].header_picture
        if(p !== undefined)
          return p
        else
          return require('../../assets/default_header.jpg')
      },
      banLogin: function(index){
      
      },
      banComment: function(index){
       
      },
      updatePassword: function(index, user){
        this.users[index].isUpdatePassword = false
        user.password = this.users[index].user.password
     
      },
      dbevent: function (index) {
        this.users[index].isUpdatePassword = true
      },
      loadBanCondition: function(){

      },
      deleteUser: function(index){
        var _this = this
        this.$confirm('删除该用户，是否继续？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消，' ,
          type: 'warning'
        }).then(()=>{
          _this.users.splice(index, 1)
          return;
        })
      }
    }
  }
</script>

<style scoped>

</style>
