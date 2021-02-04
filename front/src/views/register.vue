<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">注册</div>
      <el-form style="margin-top:20px" class="form" :model="form" status-icon :rules="rules" ref="form" label-position="right" label-width="80px" >
        <el-form-item label="用户名" :error="errorMsg" prop="userName">
          <el-input prefix-icon="el-icon-user" placeholder="请输入用户名"  v-model="form.userName" style="width:250px" clearable
          :onfocusout="mentionDoubleName"></el-input>
        </el-form-item>
        <el-form-item label="密码" :error="errorMsg" prop="userPassword">
          <el-input prefix-icon="el-icon-lock" autocomplete="off" placeholder="请输入密码" v-model="form.userPassword" type="password" style="width:250px" clearable ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :error="errorMsg" prop="userPassword2">
          <el-input prefix-icon="el-icon-lock" autocomplete="off" placeholder="请再次输入密码" v-model="form.userPassword2" type="password" style="width:250px" clearable ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click.native="register">注册</el-button>
          <el-button style="margin-left:30px" @click.native="backLogin" >返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import log from '../util/log'
  export default {
    name:'register',
    data(){
      var validateName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('用户名不能为空'));
        }
        setTimeout(() => {
          if (value.length>11) {
            callback(new Error('输入长度不得超过11'));
          }
        }, 1000);
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.form.userPasswprd2 !== '') {
            this.$refs.form.validateField('userPassword2');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value !== this.form.userPassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return{
        form:{
          userName:'',
          userPassword:'',
          userPassword2:''
        },
        errorMsg:'',
        rules:{
          userName:[
            {validator:validateName,trigger:'blur'}
          ],
          userPassword:[
            {validator:validatePass,trigger:'blur'}
          ],
          userPassword2:[
            {validator:validatePass2,trigger:'blur'}
          ]
        }
      }
    },
    methods: {
      // reSet:function(formName){
      //   this.form.userName=null;
      //   this.form.userPassword=null;
      //   this.form.userPassword2=null;
      // },
      backLogin:function(){
        this.$router.push('/login')
      },
      async mentionDoubleName(){
        let j = await log.doubleNameAlert(this.form.userName)
        if(!j){
            this.$message({
                showClose:true,
                message:'已存在用户名',
                type:'warning'
            })
        }
        //   alert('已有用户名')
      },
      async register(){
        // let j = await log.doubleNameAlert(this.form.userName)
        //   if(!j){
        //   this.$message({
        //     showClose:true,
        //     message:'已存在用户名',
        //     type:'warning'
        //   })
        // }
        // else
          if(this.form.userPassword===this.form.userPassword2){
            log.register(this.form.userName, this.form.userPassword);
            this.$message({
              showClose:true,
              message:'注册成功',
              type:'success'
            })
            this.$router.push('/login')
        }
        else{
            this.$message({
              showClose: true,
              message: '两次密码不一致',
              type: 'error'
            });
        }
      }
    }
  }
</script>

<style scoped>
  .login-wrap{
    position: absolute;
    width:100%;
    height:100%;
    top: 0;
    left: 0;
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
</style>

