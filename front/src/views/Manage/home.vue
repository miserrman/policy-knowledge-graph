<template>
    <el-container class="home_container">
        <el-header>
          <v-header></v-header>
        </el-header>
        <el-container>
            <el-aside style="width:200px;">
                <el-menu class="el-menu-vertical-demo" background-color="#ECECEC" router
                    default-active="/manage/manageUser" active-text-color="#ffd04b">
                    <el-menu-item index="/manage/manageUser">
                      <i class="el-icon-user-solid"></i>
                      <span>用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="/manage/managePolicy">
                      <i class="el-icon-question"></i>
                      <span>政策管理</span>
                    </el-menu-item>
                    <el-menu-item index="manage/manageWrite">
                      <i class="el-icon-tickets"></i>
                      <span>发布政策</span>
                    </el-menu-item>
                    <!-- <el-menu-item index="/manageData">
                      <i class="el-icon-s-grid"></i>
                      <span>政策统计</span>
                    </el-menu-item> -->
                </el-menu>
            </el-aside>
            <el-container>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right">
                        <el-breadcrumb-item :to="{ path: '/home' }">当前页</el-breadcrumb-item>
                        <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
                    </el-breadcrumb>
                    <keep-alive>
                        <router-view></router-view>
                    </keep-alive>
                </el-main>
            </el-container>
        </el-container>
    </el-container>
</template>

<script>
import vHeader from '@/components/header'
  export default{
    name:'manage',
    components: {
      vHeader
    },
    methods: {
        handleCommand(command){
            if(command==='MyHome'){
                this.$router.replace({path:'/manageUser'})
            }
            else if(command==='logout'){
                this.$router.replace({path:'/'})
                this.$message({
                    message: '退出成功',
                    type: 'success'
                });
            }
        }
    },
    mounted: function () {

    },
    data(){
        return {
            currentUserName:'admin'
        }
    }
  }
</script>
<style>
  .home_container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
  }

  .el-main {
    background-color: #fff;
    color: #000;
    text-align: center;
  }

  .el-aside {
    background-color: #ECECEC;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    display: inline;
    margin-right: 20px;
  }
</style>
