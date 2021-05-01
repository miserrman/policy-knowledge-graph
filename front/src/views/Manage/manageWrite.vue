<template>
    <div>
      <el-form style="margin-left:30px;margin:10px;text-align:left;margin-top:20px;font-weight:bolder">
                <el-form-item>
                  <el-button size="small" style="text-align:left" type="success" @click="publishArticle">发布政策</el-button>
                  <el-button size="small" style="text-align:left" type="primary">保存草稿</el-button>
                  <el-button size="small" style="text-align:left" type="danger">清空内容</el-button>
                </el-form-item>
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
                <el-form-item label="政策标题">
                    <el-input size="small" v-model="form.title" style="width:600px;margin-left:20px;" placeholder="输入您的标题"></el-input>
                    
                </el-form-item>
      </el-form>
      <quill-editor v-model="form.content"
                    ref="myQuillEditor"
                    style="margin-top:20px">
      </quill-editor>
    </div>
</template>

<script>

import axios from 'axios'
import api from "@/util/api"
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
  // you can also register quill modules in the component
  //import Quill from 'quill'
  // import { someModule } from '../yourModulePath/someQuillModule.js'
  // Quill.register('modules/someModule', someModule)

  import { quillEditor } from 'vue-quill-editor'
  import ElUpload from "element-ui/packages/upload/src/index";

  // 工具栏配置
  let visible = false
  const toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
    ['blockquote', 'code-block'],

    [{'header': 1}, {'header': 2}],               // custom button values
    [{'list': 'ordered'}, {'list': 'bullet'}],
    [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
    [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
    [{'direction': 'rtl'}],                         // text direction

    [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
    [{'header': [1, 2, 3, 4, 5, 6, false]}],

    [{'color': []}, {'background': []}],          // dropdown with defaults from theme
    [{'font': []}],
    [{'align': []}],
    ['link', 'image', 'video'],
    ['clean']                                         // remove formatting button
  ]
export default {
    name:'manageAns',
    components:{
      quillEditor
    },
    data(){
      return{
        departList: [],
        regionList: [],
        departDisabled: false,
        form: {
          depart: {
            level: "",
            region: "",
            name: ""
          },
          title: "",
          content: "",
        },
        searchStr:''
      }
    },
    mounted() {
      this.getCreateInfo()
    },
    methods: {
      changeRegion: function() {

      },
      changeCountry: function() {

      },
      publishArticle: function() {
        let _this = this
        if (this.form.title == "") {
          this.$message({
            showClose: true,
            message: "请输入政策标题",
            type: "error"
          })
        } else if (this.form.content == "") {
          this.$message({
            showClose: true,
            message: "政策内容不能为空",
            type: "error"
          })
        } else if (this.form.depart.name == "") {
          this.$message({
            showClose:true,
            message: "政策部门不能为空",
            type: "error"
          })
        } else {
           axios.post(api.publishPolicyArticle, {
              depart: _this.form.depart.level + "," + _this.form.depart.name,
              title: _this.form.title,
              content: _this.form.content
               }).then(function(response) {
                      let id = response.data.data.id
                      _this.$store.commit('setPolicyId', id)
                      _this.$router.push("/problemDetail")
                  })
        }
      },
      getCreateInfo: function() {
        let _this = this
        axios.get(api.getPolicyCreateInfo, {})
        .then(function(response) {
          let data = response.data.data
          _this.regionList = data.regions
          _this.departList = data.departs
        })
      },
      getUserName: function(userID){
        const userName = ''
      },
      handleDelete: function(index, data){
        
        this.answerList.splice(index, 1)
        console.log(this.answerList[index].answer_id)
      },
      test: function(d){
        console.log(d)
      },
      loadAnswerList: function(){
        const _this = this
       
      },

    }
}
</script>
<style>
.ql-container {
      min-height: 380px;
}
</style>
