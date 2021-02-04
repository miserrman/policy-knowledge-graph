<template>
    <div>
        <v-header></v-header>
        <switch-img></switch-img>
        <homeGuide></homeGuide>
      <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
        <span>初次注册账号获得10积分<br/><br/>积分可通过回答问题获得<br/><br/>积分可用来悬赏问题</span>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
      </el-dialog>

    </div>
</template>

<script>
import vHeader from '@/components/header'
import switchImg from '@/views/Home/switchImg'
import homeGuide from '@/views/Home/homeGuide'

export default {
    name:'home',
    components:{
        vHeader,switchImg,homeGuide
    },
    data(){
      return{
        dialogVisible: false
      }
    },
    mounted() {
      console.log(this.$store.getters.getLoginTime)
      if(this.$store.getters.getLoginTime == 1) {
        this.dialogVisible = true
        this.$store.commit('setLoginTime', this.$store.getters.getLoginTime + 1)
      }
    },
    methods: {
      handleClose: function(){
        this.dialogVisible = false
      }
    }
}
</script>

<style scoped>

</style>

