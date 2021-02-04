<template>
    <el-carousel :interval="4000" type="card" height="250px">
        <el-carousel-item v-for="item in imgBox" :key="item.id">
            <el-image :src='item.view'></el-image>
        </el-carousel-item>
    </el-carousel>
</template>

<script>
export default {
    name:'switchImg',
    data(){
        return{
            imgBox:[
                {
                    id:1,
                    view:require('@/assets/imgs/1.jpg')
                },
                {
                    id:2,
                    view:require('@/assets/imgs/2.jpg')
                },
                {
                    id:3,
                    view:require('@/assets/imgs/3.jpg')
                },
                {
                    id:4,
                    view:require('@/assets/imgs/4.jpg')
                }
            ]
        }
    }
}
</script>

<style scoped>
    .el-carousel{
        margin-top: 70px;
        margin-left:0px;
        width: 100%;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 200px;
        margin: 0px;
    }
    
    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }
    
    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }
</style>

