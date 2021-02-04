<template>
  <div>
    <el-form style="margin-top:30px;" label-position="right" label-width="80px" :model="info">
        <el-form-item>
          <el-upload
          class="avatar-uploader"
          action="/api/identify/postavator"
          :show-file-list="false"
          :on-success="handleAvatarUpload"
          :before-upload="beforeAvatarUpload">

            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>

        </el-form-item>
        <el-form-item label="用户名">
            <el-input style="width:250px;" v-model="info.name" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="性别">
            <el-radio v-model="radio" label="1" border >男</el-radio>
            <el-radio v-model="radio" label="2" border >女</el-radio>
        </el-form-item>
        <el-form-item label="电子邮箱">
            <el-input placeholder="请输入内容" style="width:250px;" v-model="info.email"></el-input>
        </el-form-item>
        <el-form-item label="电话">
            <el-input placeholder="请输入内容" style="width:250px;" v-model="info.tel"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button :plain="true" @click.native="modifyInfo" type="primary">保存修改</el-button>
        </el-form-item>

    </el-form>

  </div>
</template>

<script>
import log from '@/util/log'
import ElUpload from "element-ui/packages/upload/src/index";
import user from '@/util/user'
export default {
    name:'userinfo',
  components: {ElUpload},
  data(){
        return{
            info:{
                name: this.$store.getters.getUserName,
                email:this.$store.getters.getUserEmail,
                tel: this.$store.getters.getUserTel,
            },
            radio:'1',
          imageUrl: ''
        }
    },
    mounted(){
      this.imageUrl = this.$store.getters.getHeaderPictureURL
      this.info.tel = this.$store.getters.getUserTel
      this.info.email = this.$store.getters.getUserEmail
      if(this.info.tel == "undefined")
        this.info.tel = ''
      if(this.info.email == "undefined")
        this.info.email = ''
      if(this.imageUrl == undefined)
        this.imageUrl = require('../../assets/img.jpg')
    },
    methods:{
        modifyInfo:function(){
            this.$store.commit("setUserEmail", this.info.email)
            this.$store.commit("setHeaderPictureURL", this.imageUrl)
            this.$store.commit("setUserTel", this.info.tel)
            this.$store.commit("setUserDiscribe", this.info.describe)

            log.modifyInfor(this.$store.getters.getUserID, this.info.name, this.info.email)
            user.exmainePeopleInformation(this.$store.getters.getUserID, this.info.tel, this.url, this.info.describe)
            this.$message({
              showClose:true,
              message: '恭喜你，修改成功',
              type: 'success',
              url: ''
            });
            this.$forceUpdate()
        },
        handleAvatarUpload: function(res, file){
          this.imageUrl = URL.createObjectURL(file.raw)
          this.url = res
        },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      }
    }
}
</script>
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 100px;
    height: 100px;
    display: block;
    background-color: #8c939d;
    border-radius: 50px;
  }
</style>
