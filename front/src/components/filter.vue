<template>
<div style="text-align:left;margin:30px;font-weight:bolder">
<el-form :inline="true" :model="formInline" class="demo-form-inline">
  <el-form-item label="级别" size="small">
    <el-select placeholder="级别" size="small" style="width:100px">
      <el-option label="国家级" value="shanghai"></el-option>
      <el-option label="省级" value="beijing"></el-option>
      <el-option label="市级" value="beijing"></el-option>
      <el-option label="区县级" value="beijing"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item label="地区" size="small">
    <el-select v-model="formInline.region" placeholder="地区" style="width:150px" size="small">
      <el-option label="区域一" value="shanghai"></el-option>
      <el-option label="区域二" value="beijing"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item label="部门" size="small">
    <el-select v-model="formInline.region" placeholder="部门" size="small" style="width:300px">
      <el-option label="区域一" value="shanghai"></el-option>
      <el-option label="区域二" value="beijing"></el-option>
    </el-select>
  </el-form-item>
 <el-form-item label="发布时间" style="width:800px">
    <el-date-picker type="date" placeholder="起始日期" size="small" style="width:120px"></el-date-picker>
    <span>&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;</span>
    <el-date-picker placeholder="结束日期" size="small"  style="width:130px"></el-date-picker>
  </el-form-item>
  <el-form-item label="政策内容" size="small" style="width:480px">
    <el-input
          v-model="searchStr"
          placeholder="默认展示部分回答，可以通过关键字模糊匹配..."
          prefix-icon="el-icon-search"
          style="width: 400px" size="small">
    </el-input>
  </el-form-item> 
    <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="">搜索
        </el-button>
</el-form>
</div>
</template>
<script>
  export default {
    data() {
      return {
        formInline: {
          user: '',
          region: ''
        }
      }
    },
    methods: {
      onSubmit() {
        console.log('submit!');
      }
    }
  }
</script>
<style>
.input-select {
    height: 20px;
}
</style>