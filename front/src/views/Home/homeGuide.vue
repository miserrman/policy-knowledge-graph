<template>
  <el-container>
    <el-header>
        <el-menu :default-active="$route.path" mode="horizontal"
            text-color="#40514e" active-text-color="black" router>
            <el-menu-item style="margin-left:280px;" index="/all">
              <i class="el-icon-menu"></i>
              <span>全部</span>
            </el-menu-item>
            <el-menu-item index="/economy">
              <i class="el-icon-s-platform"></i>
              <span>经济</span>
            </el-menu-item>
            <el-menu-item index="/science">
              <i class="el-icon-camera-solid"></i>
              <span>科教</span>
            </el-menu-item>
            <el-menu-item index="/environment">
              <i class="el-icon-basketball"></i>
              <span>环境</span>
            </el-menu-item>
            <el-menu-item index="/tour">
              <i class="el-icon-goblet-full"></i>
              <span>文旅</span>
            </el-menu-item>
            <el-menu-item index="/society">
              <i class="el-icon-monitor"></i>
              <span>社会</span>
            </el-menu-item>
            <el-menu-item index="/info">
              <i class="el-icon-user"></i>
              <span>信息</span>
            </el-menu-item>
        </el-menu>
    </el-header>
    <el-main style="margin-top:10px;">
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
    </el-main>
  </el-container>
</template>

<script>
export default {
    name:'homeGuide',
    data(){
        return{

        }
    },
    methods:{

    }
}
</script>

<style scoped>

</style>
