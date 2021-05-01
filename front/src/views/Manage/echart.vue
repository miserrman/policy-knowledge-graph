<template>
    <div id="echartContainer" style="width:800px; height:500px;"></div>
</template>

<script>
var echarts = require('echarts')
import manager from '@/util/manager'
export default {
    name:'echart',
    data(){
      return{
        kinds: ["所有","科学","数码","体育","时尚","影视","生活","游戏"],
        questionNum: []
      }
    },
    mounted() {
        let k = this.kinds.join(',')
        manager.getKindNum(k, function(arr){
          console.log(arr)
          var myChart = echarts.init(document.getElementById('echartContainer'));
          // 绘制图表
          myChart.setOption({
            title: { text: '问题种类下问题数量' },
            tooltip: {},
            xAxis: {
              data: ["全部","科学","数码","体育","时尚","影视","生活","游戏"]
            },
            yAxis: {},
            series: [{
              name: '销量',
              type: 'bar',
              data: arr
            }]
          });
        })
        // 基于准备好的dom，初始化echarts实例

    }
}
</script>

<style scoped>

</style>
