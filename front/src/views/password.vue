<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">设置新密码</div>
            <el-form style="margin-top:20px; " label-width="80px" label-position="right" :model="form">
                <el-form-item label="新密码">
                    <el-input prefix-icon="el-icon-key" style="width:250px;" placeholder="请输入新密码" v-model="form.password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                    <el-input prefix-icon="el-icon-key" style="width:250px;" placeholder="再次输入确认密码" v-model="form.checkedPassword"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button @click.native="setPass" type="primary" style="margin-left:80px;width:200px;">设置密码</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
  import log from '@/util/log'
export default {
    name:'password',
    data(){
        return{
            form:{
                password:'',
                checkedPassword:''
            }
        }
    },
    methods:{
        setPass:function(){
            if(!this.form.password || !this.form.checkedPassword){
                this.$message({
                    showClose:true,
                    message:'密码不能为空',
                    type:'error'
                })
            }
            else if(this.form.password !== this.form.checkedPassword){
                this.$message({
                    showClose:true,
                    message:'两次密码输入不一致',
                    type:'error'
                })
                this.form.password = null
                this.form.checkedPassword = null
            }
            else{

                log.modifyPassword(this.$store.getters.getUserID)
                this.$message({
                    showClose:true,
                    message:'新密码设置成功',
                    type:'success'
                })
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
  .login-btn button{
    width:100%;
    height:36px;
    margin-bottom: 10px;
  }
</style>
