<template>
    <el-container class="home_container">
        <el-container>
            <el-aside style="width:200px;">
                <el-menu class="el-menu-vertical-demo" style="background-color: #ECECEC" router
                    :default-active="$route.path" theme="dark">
                    <el-menu-item index="/hostPage">
                        <i class="el-icon-s-home"></i>
                        <span>个人主页</span>
                    </el-menu-item>
                    <el-menu-item index="/userinfo">
                        <i class="el-icon-user-solid"></i>
                        <span>个人信息</span>
                    </el-menu-item>
                    <el-menu-item index="/myAsk">
                        <i class="el-icon-phone"></i>
                        <span>公司信息</span>
                    </el-menu-item>
                    <el-menu-item index="/myCollect">
                        <i class="el-icon-star-on"></i>
                        <span>我的收藏</span>
                    </el-menu-item>
                    <el-menu-item index="/myFollow">
                        <i class="el-icon-view"></i>
                        <span>我的关注</span>
                    </el-menu-item>
                    <el-menu-item index="/myNotice">
                        <i class="el-icon-s-promotion"></i>
                        <span>我的消息</span>
                    </el-menu-item>
                    <el-menu-item index="/modifyPass">
                        <i class="el-icon-s-tools"></i>
                        <span>修改密码</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-container>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right">
                        <el-breadcrumb-item :to="{ path: '/userpage' }">个人中心</el-breadcrumb-item>
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
export default {
    name:'sidebar',
    data(){
        return{

        }
    },
    computed:{
// 处理defaultActive，让其与路由相关联，每次刷新页面定位到当前路由
    },
    methods:{

    }
}
</script>

<style scoped>
    .home_container {
        height: 100%;
        position: absolute;
        top: 60px;
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
</style>

