<template>
  <!-- bidirectional data binding（双向数据绑定） -->
  <div class="note-area">
    <Header></Header>
    <div style="margin-top: 100px;">
      <div style="margin:0 auto;width:800px">
        <el-form ref="form" model="form" label-width="80px">
          <el-form-item label="政策标题">
          <el-input style="width:400px"></el-input>
        </el-form-item>
        </el-form>
        <el-form ref="form" model="form" label-width="80px" inline="true">
         <el-form-item label="政策层级">
    <el-select placeholder="政策层级" style="width:200px">
      <el-option label="国家级" value="shanghai"></el-option>
      <el-option label="省级" value="beijing"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item label="政策部门" style="width:400px">
    <el-select placeholder="政策部门">
      <el-option label="福建省工业信息部门" value="shanghai"></el-option>
      <el-option label="福建省人民政府" value="beijing"></el-option>
    </el-select>
  </el-form-item>
        </el-form>
      </div>
      <quill-editor v-model="content"
                    ref="myQuillEditor"
                    :options="editorOption"
                    @change="onEditorChange">
      </quill-editor>
      <el-dialog :visible="vis" :before-close="handleClose"
      title="提示">
        <el-upload class="upload-demo"
        action="/api/answer/articlepicture"
        :show-file-list="false"
        :on-success="handleAvatarUpload"
        :before-upload="beforeAvatarUpload">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-button @click="insertThePicture"></el-button>
      </el-dialog>
    </div>
  </div>

</template>

<script>
  // require styles
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  import answers from '@/util/answer'
  import Header from '@/components/header'
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
    name:'writeAns',
    components:{
      ElUpload,
      Header,
      quillEditor
    },
    data () {
      let _this = this
      return {
        content: '<h2>I am Example</h2>',
        vis: false,
        imageUrl: '',
        file: '',
        editorOption: {
          modules: {
            toolbar: {
              container: toolbarOptions,  // 工具栏
              handlers: {
                'image': function(value){
                    _this.vis = true

                }
              }
            }
          }
        }
      }
    },
    props: ['questionID'],
    // manually control the data synchronization
    // 如果需要手动控制数据同步，父组件需要显式地处理changed事件
    methods: {
      insertThePicture: function(){
        let quill = this.$refs.myQuillEditor.quill
        let length = quill.getSelection()
        let _this = this
          quill.insertEmbed(length, 'image', _this.url)
          quill.setSelection(length + 1)
      },
      commitAnswer: function(){
        answers.commitAnswer(this.$store.getters.getUserID,this.$store.getters.getQuestionID, this.content)
        this.$router.go(-1)
      },
      onEditorBlur(quill) {
        console.log('editor blur!', quill)
        console.log(quill.container.firstChild.innerHTML)//获得html格式文本,岂不美哉
      },
      onEditorFocus(quill) {
        console.log('editor focus!', quill)
      },
      onEditorReady(quill) {
        console.log('editor ready!', quill)
      },
      onEditorChange({ quill, html, text }) {
        this.content = html
        console.log(this.content)
      },
      handleClose: function () {
        console.log(this.fileList)
        this.vis = false
      },
      handleRemove: function (file, fileList) {
        console.log(fileList)
      },
      handleAvatarUpload: function(res, file){
        this.file = file
        this.imageUrl = URL.createObjectURL(file.raw)
        this.url = res
        let quill = this.$refs.myQuillEditor.quill
        let length = quill.getSelection()
        let _this = this
        quill.insertEmbed(length, 'image', _this.url)
        quill.setSelection(length + 1)
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      }
    },
    computed: {
      editor() {
        return this.$refs.myQuillEditor.quill
      }
    },
    mounted() {
      console.log('this is current quill instance object', this.editor)
    },
  }
</script>
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    background-color: #8c939d;
  }
  .el-row{
    width: 800px;
    margin: 0 auto;
  }
  .quill-editor{
    width: 800px;
    height: 500px;
    margin: 0 auto;
  }
</style>
