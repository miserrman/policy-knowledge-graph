<template>
    <el-card>
      <div slot="header" style="height: 20px;padding: 0px">
        <span style="font-size: 12px;">{{commentList.length}}条评论</span>
      </div>
      <div class="comment-detail" v-for="(comment,index) in commentList">
        <img :src="comment.header_picture"/>
        <span>{{comment.user.userName}}</span>
        <p>{{comment.comment.comment}}</p>
      </div>
      <div class="footer">
        <el-input v-model="content" size="mini" style="width: 85%;"></el-input>
        <el-button size="mini" style="margin-top: 5px" @click="comment">发布</el-button>
      </div>
    </el-card>
</template>

<script>
  import user from '@/util/user'
  import comment from '@/util/comment'
  import answer from '@/util/answer'
    export default {
        name: "comment",
        props: ['answerID'],
        data(){
          return{
            commentList: [],
            content: ''
          }
        },
      mounted() {
        this.loadComment()
      },
      methods: {
        loadComment: function(){
          let _this = this
          console.log(this.answerID)
          comment.getAllComment(this.answerID, function(result){
            let that = _this
            console.log(result)
              result.forEach(data=>{
                user.getUserInfo(data.user_id, function(res){
                 user.getOtherDiscribe(data.user_id, function(describe){
                   let info = {
                     comment: '',
                     user: '',
                     header_picture: ''
                   }
                   if(describe == undefined || describe.header_picture == undefined){
                     info.header_picture = require('../assets/default_header.jpg')
                   }
                   else{
                     info.header_picture = describe.header_picture
                   }

                   info.comment = data
                   info.user = res
                   that.commentList.push(info)
                 })
                })
              })
          })
        },
        comment: function(){
          comment.commentAnswer(this.answerID, this.$store.getters.getUserID, this.content, function(response){
          })
          let commentAnswer = {
            comment: {
              comment: this.content
            },
            user: {
              userName: this.$store.getters.getUserName
            },
            header_picture: this.$store.getters.getHeaderPictureURL
          }
          if(commentAnswer.header_picture == "undefined")
            commentAnswer.header_picture = require('../assets/default_header.jpg')
          this.commentList.push(commentAnswer)
          this.content = ''
        }
      }
    }
</script>

<style scoped>
.comment-detail img{
  float: left;
  width: 20px;
  height: 20px;
  margin-top: 10px;
}
.comment-detail span{
  margin-top: 20px;
  margin-left: 10px;
  position: relative;
  top: 5px;
}
.comment-detail{
  border-bottom: 0.05px solid #ddd ;
}
.comment-detail p{
  margin-top: 12px;
  margin-left: 30px;
  font-size: 12px;
}
  .el-card__header{
    padding: 0;
  }
  .footer{
    margin-top: 5px;
  }
</style>
