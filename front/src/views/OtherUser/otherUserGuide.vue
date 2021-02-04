<template>
    <el-container>
      <el-header>
        <el-menu :default-active="$route.path" mode="horizontal"
                 text-color="#40514e" active-text-color="black" router>
          <el-menu-item index="/otherUserStore">
            <i class="el-icon-star-on"></i><span>他的收藏</span>
          </el-menu-item>
          <el-menu-item index="/otherUserAsk">
            <i class="el-icon-question"></i><span>他的问题</span>
          </el-menu-item>
          <el-menu-item index="">
            <i class="el-icon-s-promotion"></i><span>他的回答</span>
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
    import ElHeader from "element-ui/packages/header/src/main";
    export default{
        name: "otherUserGuide",
      components: {ElHeader}
    }
</script>

<style scoped>

</style>
