<template>
    <el-form style="margin-top:30px;" label-position="right" label-width="80px" :model="form">
        <el-form-item label="老密码">
            <el-input type="password" placeholder="请输入内容" style="width:250px;" v-model="form.oldPass"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
            <el-input type="password" placeholder="请输入内容" style="width:250px;" v-model="form.newPass"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
            <el-input type="password" placeholder="请输入内容" style="width:250px;" v-model="form.checknewPass"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button :plain="true" @click.native="modifyPass" type="primary">确认修改</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
import log from '@/util/log'
export default {
    name:'modifyPass',
    data(){
        return{
            form:{
                oldPass:'',
                newPass:'',
                checknewPass:''
            },
          rules:{

          }
        }
    },
    methods:{
        modifyPass:function(){
          if(this.form.oldPass === this.$store.getters.getUserPassword){
            if(this.form.newPass === this.form.checknewPass){
              let username = this.$store.getters.getUserName;
              let email = this.$store.getters.getUserEmail;
              log.modifyPassword(this.$store.getters.getUserID, this.form.newPass)
              this.$message({
                showClose:true,
                message: '恭喜你，修改成功',
                type: 'success'
              });
              this.$router.push('/login')
            }
            else
              this.$message({
                showClose: true,
                message: '两次密码不一致',
                type: 'error'
              })
          }
          else{
            this.$message({
              showClose: true,
              message: '密码输入错误',
              type: 'error'
            })
          }
        }
    }
}
</script>

<style scoped>

</style>
