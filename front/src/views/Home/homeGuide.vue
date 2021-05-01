<template>
    <div>
    <div class="home-guide">
            <el-menu :default-active="$route.path" mode="horizontal"
                     text-color="#40514e" active-text-color="black" router>
                <el-menu-item index="/all">
                    <i class="el-icon-menu"></i>
                    <span>政策动态</span>
                </el-menu-item>
                <el-menu-item index="/economy">
                    <i class="el-icon-s-platform"></i>
                    <span>法律法规</span>
                </el-menu-item>
                <el-menu-item index="/science">
                    <i class="el-icon-camera-solid"></i>
                    <span>申报日历</span>
                </el-menu-item>
                <el-menu-item index="/environment">
                    <i class="el-icon-basketball"></i>
                    <span>今日推荐</span>
                </el-menu-item>
            </el-menu>
            <div class="main-content">
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
        </div>
        </div>
    <div class="right-nav">
        <el-card>
            <div class="right-pic">
                <img src="../../assets/house.jpg">
                <h3>政策单位</h3>
            </div>
            <br>
            <div class="right-pic">
                <img src="../../assets/book.jpg">
                <h3>企业匹配</h3>
            </div>
            <br>
            <div class="right-pic" @click="writePolicy">
                <img src="../../assets/pencil.jpg">
                <h3>发布政策</h3>
            </div>
        </el-card>
    </div>
    </div>
</template>

<script>
export default {
    name:'homeGuide',
    data(){
        return{

        }
    },
    methods:{
        writePolicy: function() {
            this.$router.push("/writeAns")
            console.log("dscsdc")
        }
    }
}
</script>

<style scoped>
    .home-guide {
        margin-left: 15%;
        position: relative;
        float: left;
        width: 60%;
    }
    .main-content {
        float: left;
    }
    .right-nav {
        width: 250px;
        float: left;
        margin-right: 30px;
        
    }
    .right-pic:hover {
        color: blue;
        cursor: pointer;
    }
    .right-pic {
        margin-top: 20px;
        position: relative;
        bottom: 20px;
        height: 20px;
    }
    .right-pic img{
        float: left;
        border-radius: 50%;
        width: 50px;
        height: 50px;
    }
    .right-pic h3{
        margin-left: 70px;
        position: relative;
        top: 10px;
    }

</style>
