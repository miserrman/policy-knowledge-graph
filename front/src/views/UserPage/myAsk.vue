<template>
    <div style="text-align: center;margin: 0 auto;" >
      <div style="margin:40px; text-align:left">
        <div style="float:left;width:400px">
          <span class="company-info-title">公司名称：</span>
          <span class="company-info">{{enterprise.name}}</span>
          <br/>
          <br/>
          <span class="company-info-title">所在区域：</span>
          <span class="company-info">{{enterprise.region}}</span>
          <br/>
          <br/>
          <span class="company-info-title">科研人员数：</span>
          <span class="company-info">{{enterprise.researchersNum}}</span>
          <br/>
          <br/>
          <span class="company-info-title">年纯利润：</span>
          <span class="company-info">{{enterprise.profit}}万元</span>
        </div>
        <div style="float:left;width:400px">
            <span class="company-info-title">研发费用：</span>
            <span class="company-info">{{enterprise.researchExpense}}万元</span>
            <br/>
            <br/>
            <span class="company-info-title">研发收入：</span>
            <span class="company-info">{{enterprise.techOutfit}}万元</span>
            <br/>
            <br/>
             <span class="company-info-title">规模：</span>
            <span class="company-info">{{enterprise.scale}}</span>
            <br/>
            <br/>
            <span class="company-info-title">所获证书：</span>
            <span class="company-info">{{enterprise.certificates}}</span>
        </div>
      
      </div>
    </div>
</template>

<script>
import ScoreDistribute from "@/components/ScoreDistribute";
import axios from 'axios'
import api from '@/util/api'
export default {
    name:'myAsk',
    components: {ScoreDistribute},
    data() {
      return {
        enterprise: {}
      }
    },
    mounted() {
      let _this = this
      axios.get(api.getUserEnterprise, {
        headers: {
          userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
        }
      }).then(function(response) {
        _this.enterprise = response.data.data
        if (_this.enterprise.scale == 0) {
          _this.enterprise.scale = "微型"
        }else if (_this.enterprise.scale == 1) {
          _this.enterprise.scale = "小型"
        }else if (_this.enterprise.scale == 2) {
          _this.enterprise.scale = "中型"
        }
      })
    },
    methods: {

    }
}
</script>
<style>
.company-info-title {
  font-size: 14px;
  font-weight: bolder;
}
.company-info {
  font-size: 14px;
  margin-right: 100px;
}
</style>