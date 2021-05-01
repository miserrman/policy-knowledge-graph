<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">龙岩政策云平台</div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="ms-content">
        <el-form-item prop="username">
          <el-input v-model="ruleForm.username" placeholder="username" clearable >
            <el-button slot="prepend" icon="el-icon-user"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')" clearable >
            <el-button slot="prepend" icon="el-icon-lock"></el-button>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click.native="submitForm('ruleForm')">登录</el-button>
        </div>
        <div>
            <el-button style="color:black" type="text" @click.native="searchPass">Forget Password?</el-button>
        </div>
        <div>
            <el-button style="color:black" type="text" @click.native="toRegister">还没注册过，现在去注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
  import log from '../util/log'
  import message from '@/util/message'
  import axios from 'axios'
  import api from '@/util/api'
  export default {
    name:'login',
    data: function(){
      return {
        ruleForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      submitForm: function(formName) {
        let _this = this
        if (this.ruleForm.username && this.ruleForm.password) {
          axios.get(api.login, {
            params: {
              userName: _this.ruleForm.username,
              password: _this.ruleForm.password
            }
          }).then(function(response) {
            let data = response.data
            if (data.errno === 0) {
              sessionStorage.setItem("userInfo", data.data)
              localStorage.setItem("role", data.data.role)
              localStorage.setItem("userName", data.data.userName)
              localStorage.setItem("userId", data.data.id)
              localStorage.setItem("email", data.data.email)
              localStorage.setItem("tele", data.data.tele)
              _this.$store.commit("setUserInfo", data.data)
              console.log(data.data.id)
              _this.$router.push("/home")
            }
          })
        }
      },
      searchPass:function(){
          // this.$message({
          //     showClose:true,
          //     message:'该功能暂未开放',
          //     type:'success'
          // })
          const now = new Date()
          console.log(now.toLocaleString())
          this.$router.push('/retrievePassword')
      },
      toRegister:function(){
          this.$router.push('/register')
      }
    }
  }
</script>

<style scoped>
  .login-wrap{
    position: absolute;
    width:100%;
    height:100%;
    top:0;
    left:0;
    background-image: url(../assets/login-bg.jpg);
    background-size: 100%;
  }
  .ms-title{
    width:100%;
    line-height: 50px;
    text-align: center;
    font-size:28px;
    color:black;
    border-bottom: 1px solid #ddd;
  }
  .ms-login{
    position: absolute;
    left:50%;
    top:50%;
    width:350px;
    margin:-190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255,255,255, 0.3);
    overflow: hidden;
  }
  .ms-content{
    padding: 30px 30px;
  }
  .login-btn{
    text-align: center;
  }
  .login-btn button{
    width:100%;
    height:36px;
    margin-bottom: 10px;
  }
</style>
