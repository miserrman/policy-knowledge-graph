<template>
    <div>
      <div style="text-align:left;margin:30px;font-weight:bolder">
<el-form :inline="true" class="demo-form-inline">
  <el-form-item label="发布部门">
                    <el-select placeholder="选择层级" v-model="form.depart.level" style="width:100px;margin-right:20px;margin-left:20px" size="mini" @change="changeCountry">
                      <el-option label="国家级" value="0"></el-option>
                      <el-option label="省市级" value="1"></el-option>
                      <el-option label="地区级" value="2"></el-option>
                    </el-select>
                    <el-select :disabled="departDisabled" placeholder="选择地区" size="mini" v-model="form.depart.region" style="width:150px;margin-right:20px" @change="changeRegion">
                      <el-option v-for="(region, index) in regionList" :label="regionList[index]" :value="regionList[index]"></el-option>
                    </el-select>
                    <el-select placeholder="选择部门" size="mini" v-model="form.depart.name" style="width:300px">
                      <el-option v-for="(depart, index) in departList" :label="departList[index].name" :value="departList[index].name"></el-option>
                    </el-select>
                </el-form-item>
 <el-form-item label="发布时间" style="width:800px">
    <el-date-picker type="date" placeholder="起始日期" size="small" style="width:120px" v-model="form.startTime"></el-date-picker>
    <span>&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;</span>
    <el-date-picker placeholder="结束日期" v-model="form.endTime" size="small"  style="width:130px"></el-date-picker>
    <el-radio-group v-model="form.code">
      <el-radio label="显示隐藏"></el-radio>
      <el-radio label="不显示隐藏"></el-radio>
    </el-radio-group>

  </el-form-item>
  <el-form-item label="政策内容" size="small" style="width:480px">
    <el-input v-model="form.title"
          placeholder="默认展示部分回答，可以通过关键字模糊匹配..."
          prefix-icon="el-icon-search"
          style="width: 400px" size="small">
    </el-input>
  </el-form-item> 
    <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="searchPolicies">搜索
      </el-button>
</el-form>
</div>
       <el-table :data="this.policyViewList">
        <el-table-column type="expand">
          <template slot-scope="props">
                <div  v-html="props.row.policyTitle.id">
                  {{props.row}}
                </div>
          </template>
        </el-table-column>
        <el-table-column prop="policyTitle.id"label="政策ID" width="70px">
        </el-table-column>
        <el-table-column prop="policyTitle.depart" label="发布部门" width="160px"></el-table-column>
        <el-table-column prop="policyTitle.date" label="发布时间" width="150px"></el-table-column>
        <el-table-column prop="policyTitle.title" label="政策标题" width="320px"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="topPolicy(scope.$index)"
              :disabled="policyViewList[scope.$index].policyManage != null && policyViewList[scope.$index].policyManage.code == 2">
              <span v-if="policyViewList[scope.$index].policyManage != null && policyViewList[scope.$index].policyManage.code == 1">归位</span>
              <span v-else>置顶</span>
              </el-button>
              <el-button
              size="mini"
              type="success"
              @click="hidePolicy(scope.$index)">
              <span v-if="policyViewList[scope.$index].policyManage != null && policyViewList[scope.$index].policyManage.code == 2">显示</span>
              <span v-else>隐藏</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>      
      <div class="block" style="height:100px">
        <el-pagination
            :page-count="pageTotal"
            @current-change="pageViewChange"
            layout="prev, pager, next">
        </el-pagination>
      </div>
    </div>
</template>

<script>
import filterPolicy from '@/components/filter'
import axios from 'axios'
import api from '@/util/api'
const kindOptions = ['科学','数码','体育','时尚','影视','生活','游戏']

export default {
  name: 'manageProblem',
  components: {
    filterPolicy
  },
  data() {
    return {
      form: {
        depart: {
          level: "",
          region: "",
          name: ""
        },
        title: "",
        startTime: "",
        endTime: "",
        code: "不显示隐藏"
      },
      pageTotal: 90,
      pageCount: 10,
      policyList: [],
      questions: [],
      policyViewList: [],
      searchStr:'',
      kinds:kindOptions,
      checkedKinds:[],   //从后端获取
      currentPage: 1,
      regionList: [],
      departList: []
    }
  },
  mounted() {
    this.searchPolicies()
    this.getCreateInfo()
  },
  methods: {
     getCreateInfo: function() {
        let _this = this
        axios.get(api.getPolicyCreateInfo, {})
        .then(function(response) {
          let data = response.data.data
          _this.regionList = data.regions
          _this.departList = data.departs
        })
      },
    topPolicy: function(index) {
      if (this.policyList[(this.currentPage - 1) * this.pageCount + index].policyManage == null || this.policyList[(this.currentPage - 1) * this.pageCount + index].policyManage.code != 1) {
        this.policyViewList.splice(index, 1)
        const p = this.policyList[(this.currentPage - 1) * this.pageCount + index]
        this.policyList.splice((this.currentPage - 1) * this.pageCount + index, 1)
        this.policyList.unshift(p)
        this.policyList[0].policyManage = {
          policyId: this.policyList[0].policyTitle.id,
          code: 1,
          index: (this.currentPage - 1) * this.pageCount + index
        }
        let _this = this
        this.pageViewChange(this.currentPage)
        axios.post(api.managePolicy, {
            policyId: p.policyTitle.id,
            code: 1
          })
      }
      else {
        const p = this.policyList[(this.currentPage - 1) * this.pageCount + index]
        this.policyList.splice((this.currentPage - 1) * this.pageCount + index, 1)
        this.policyViewList.splice(index, 1)
        for (let i = 0; i < this.policyList.length; i++) {
          if (this.policyList[i].policyManage != null && this.policyList[i].policyManage.code == 1)
            continue
          if (this.policyList[i].policyTitle.id > p.policyTitle.id) {
            this.policyList.splice(i, 0, p)
            this.policyList[i].policyManage = null
            break
          }
        }
        this.pageViewChange(this.currentPage)
        axios.post(api.managePolicy, {
            policyId: p.policyTitle.id,
            code: 0
          })
      }
      
    },
    hidePolicy: function(index) {
          this.policyViewList.splice(index, 1)
          const p = this.policyList[(this.currentPage - 1) * this.pageCount + index]
          this.policyList.splice((this.currentPage - 1) * this.pageCount + index, 1)
          let code = 2
          if (p.policyManage != null && p.policyManage.code == 2) {
            code = 0
          }
          axios.post(api.managePolicy, {
            policyId: p.policyTitle.id,
            code: code
          })
        
    },
    pageViewChange: function(currentPage) {
      this.policyViewList = []
      this.pageTotal = Math.floor(this.policyList.length / this.pageCount) + 1
      for (let i = (currentPage - 1) * 10; i < Math.min(currentPage * 10, this.policyList.length); i++) {
        this.policyViewList.push(this.policyList[i])
      }
      this.currentPage = currentPage
    },
    tagSplit: function(tag){
      return tag.split(tag)
    },
    kindShowInput: function(index){
      this.questions[index].kindInputVisible = true
    },
    kindHandleClose: function(index){
      this.questions[index].complement.splice(index, 1)
    },
    handleInputConfirm: function(index){
      this.questions[index].inputVisible = false
      this.questions[index].label_m.push(this.questions[index].inputValue)
      const l = this.questions[index].label_m.join(',')
    },
    kindHandleInputConfirm: function(index){
      this.questions[index].kindInputVisible = false
      this.questions[index].complement.push(this.questions[index].kindInputValue)
    },
    showInput: function(index){
      this.questions[index].inputVisible = true
    },
    manageQuestion: function(index){
      this.questions[index].manage = !this.questions[index].manage
    },
    handleClose: function(label, index){
      this.questions[index].label_m.splice(this.questions[index].label_m.indexOf(label), 1)
      const l = this.questions[index].label_m.join(',')
    },
    deleteQuestion: function(index){
      this.$confirm("将要删除此问题，是否继续?", "提示", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.questions.splice(index, 1)
        this.$message({
          showClose: true,
          type: 'success',
          message: '删除成功'
        })
      })
    },
    manageComplement: function(index){
     
    },
    searchPolicies: function(){
      this.policyViewList = []
      this.policyList = []
      const _this = this
      let departId = null
      if (this.form.depart.name != "") {
        for (let i = 0; i < this.departList.length; i++) {
          if (this.departList[i].name == this.form.depart.name) {
            departId = this.departList[i].id
            break
          }
        }
      }
      let title = null
      if (this.form.title != "")
        title = this.form.title
      let code = null
      if (this.form.code != "不显示隐藏") {
        code = 2
      }
      let startDate = null
      if (this.form.startTime != "")
        startDate = this.form.startTime
      let endDate = null
      if (this.form.endTime != "")
        endDate = this.form.endTime
      axios.get(api.getManagePolicyList, {
        params: {
          title: title,
          departId: departId,
          code: code,
          startDate: startDate,
          endDate: endDate
        }
      }).then(function(response) {
        _this.policyList = response.data.data
        _this.pageTotal = Math.floor(_this.policyList.length / _this.pageCount) + 1
        for (let i = 0; i < Math.min(10, _this.policyList.length); i++)
          _this.policyViewList.push(_this.policyList[i])
      })
    }
  }
}
</script>

<style scoped>
.el-header p{
  font-size: 14px;
}
  .el-card__body{
    padding: 0;
  }
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>

