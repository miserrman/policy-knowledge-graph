<!--用户点击进去的题头组件-->
<!--不知道绑定会不会成功。。。。-->
<template>
  <div>
    <div class="question-detail-h">
    <el-container>
      <el-header style="height: 20px;padding: 2px">
        <el-button round size="mini" v-for="talktag in talkTagList" >{{talktag}}</el-button>
      </el-header>
      <el-main>
        <h4>{{title}}</h4>
        <div>
          {{text}}
        </div>
      </el-main>
      <el-footer style="height: 30px">
        <el-button type="primary" size="mini">关注问题</el-button>
        <el-button type="primary" plain size="mini">
          <i class="el-icon-edit">写回答</i>
        </el-button>
        <i class="el-icon-chat-dot-round" style="margin-left: 10px;font-size: 12px">{{commentNum}}条评论</i>
      </el-footer>
    </el-container>
    </div>
    <div class="answerFrame">
      <el-container>
        <el-header style="height: 20px;">
          <span>{{commentNum}}个回答</span>
          <div>
          <span v-on:click="sortWay" style="float: right" class="sort-style">
            <p v-if="isSortDefault">默认排序</p>
            <p v-if="!isSortDefault">按时间排序</p>
          </span>
            <i class="el-icon-d-caret" style="float: right"></i>
          </div>
        </el-header>
      </el-container>
    </div>
  </div>
</template>

<script>
    export default {
        name: "QuestionDetail",
      components: {},
      props: {
          talkTagList: {
            type: Array,
            default: function(){
              return ['测试', '测试']
            }
          },
          title: {
            type: String,
            default: '测试标题，一般用于测试'
          },
          text: {
            type: String,
            default: '测试文章，一般来说测试文章要看一看要不要分行哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈'
          },
          commentNum: {
            type: Number,
            default: 0
          }
      },
      component: '@component/talkTag',
        data() {
          return {
            isSortDefault: true
          }
        },
      methods: {
          sortWay: function(){
            this.isSortDefault = !this.isSortDefault
          },

      }
    }
</script>

<style scoped>
  .question-detail-h{
    width: 30%;
    min-width: 500px;
    padding: 10px;
    border-bottom: 1px solid;
    border-color: #8c939d;
  }
  
  .answerFrame{
    margin-top: 10px;
  }
  .el-main{
    padding: 2px;
  }
  .el-footer{
    margin-top: 10px;
    padding: 2px;
    height: 30%;
  }
  .el-footer .el-button{
  }
  .el-main h{
    font-size: 14px;
    font-weight: bold;
  }
  .el-main h4{
    margin-top: 30px;
    margin-bottom: 5px;
    font-weight: bolder;
  }
  .el-main div{
    font-size: 12px;
  }
  .el-main div{
    word-wrap: break-word;
  }


  .answerFrame{
    width: 30%;
  }
  .el-header span{
    font-size: 12px;
    font-weight: bolder;
  }
  .el-header div{
    float: right;
    font-size: 12px;
  }
  .sort-style{
    cursor: pointer;
  }
  .sort-style p{
    margin: 3px;
  }
  .el-icon-d-caret{
    margin: 4px;
    position: relative;
    left: 10px;
  }
</style>
