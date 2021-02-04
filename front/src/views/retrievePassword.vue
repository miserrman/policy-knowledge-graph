<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">找回密码</div>
            <el-form style="margin-top:20px; " label-width="80px" label-position="right" :model="form">
                <el-form-item label="用户名" prop="userName">
                    <el-input prefix-icon="el-icon-user-solid" style="width:250px;" placeholder="请输入用户名" v-model="form.userName"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input prefix-icon="el-icon-s-promotion" style="width:250px;" placeholder="请输入绑定的邮箱" v-model="form.email"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click.native="getEmail" type="primary">找回密码</el-button>
                    <el-button @click="backLogin">返回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
  import axios from 'axios'
  import user from "@/util/user";
export default {
    name:'retrievePassword',
    data(){
        return{
            form:{
                userName:'',
                email:'',
            },
            linkStr:'http://localhost:8084/#/password'
        }
    },
    methods:{
        getEmail:function(){
            this.$store.commit('setUserEmail',this.form.email)
            this.$store.commit('setUserName', this.form.userName)
            const _this = this
          axios.get(`/api/identify/forgetpassword?userName=${this.$store.getters.getUserName}&email=${this.form.email}`)
              .then(function(res){
                user.getUser(_this.form.userName, function(u){
                  _this.$store.commit('setUserID', u.user_id)
                })
              })
              .catch(function(err){})
            this.$message({
                showClose:true,
                message:'修改密码链接已发至邮箱',
                type:'success'
            })

        },
        backLogin:function(){
            this.$router.push('/login')
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
