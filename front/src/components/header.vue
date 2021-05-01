<template>
    <div class="header">
        <div class="query">
            <el-dialog :visible.sync="queryForm" style="width: 1400px;">
            <el-form :model="askForm" label-position="right">
                <el-form-item label="问题叙述">
                    <el-col :span="20">
                        <el-input type="textarea" v-on:focus="getFoucus" v-model="askForm.content" :rows="5" style="border: 0"
                        placeholder="请输入问题叙述（100字以内）" :autosize="{ minRows: 1, maxRows: 3}" maxlength="100" show-word-limit clearable ></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="问题分类">
                    <el-col :span="20">
                        <el-checkbox-group v-model="askForm.dynamicKinds" :min="1" :max="2">
                            <el-checkbox v-for="kind in kinds" :label="kind" :key="kind" >{{kind}}</el-checkbox>
                        </el-checkbox-group>
                    </el-col>
                </el-form-item>
                <el-form-item label="问题标签">
                    <el-col :span="20">
                        <el-tag type="success" :key="tag" v-for="tag in askForm.dynamicTags" closable :disable-transitions="false" @close="handleCloseTag(tag)">
                            {{tag}}
                        </el-tag>
                        <el-input
                            class="input-new-tag"
                            v-if="inputVisibleTag"
                            v-model="inputValue"
                            ref="saveTagInput"
                            size="small"
                            @keyup.enter.native="handleInputConfirmTag"
                            @blur="handleInputConfirmTag">
                        </el-input>
                        <el-button type="success" v-else class="button-new-tag" size="small" @click.native="showInputTag">+ New Tag</el-button>
                    </el-col>
                </el-form-item>
                <el-form-item label="问题积分">
                    <el-col :span="10">
                        <el-input-number placeholder="请输入积分" v-model="askForm.reward" controls-position="right" :min="0" :max="this.$store.getters.getUserScore" ></el-input-number>
                    </el-col>
                </el-form-item>
                <el-form-item>
                <el-button size="small" type="primary" @click.native="commitQuestions">发布提问</el-button>
                </el-form-item>

            </el-form>
            </el-dialog>
        </div>

        <el-row >
            <el-col :span="2" >
              <el-image  style="margin-left:50px;margin-top:5px;width:50%;height:50%;":src="require('../assets/imgs/logo.png')" fit="cover"></el-image>
            </el-col>
            <el-col :span="2" >
              <el-image  style="margin-top:10px;width:100%;height:100%;":src="require('../assets/imgs/logo-name.png')" fit="cover"></el-image>
            </el-col>

            <el-col :span="1" offset="1">
              <!-- <el-button style="margin-top:10px; font-size:18px;" @click.native="gotoHome" type='text'>首页</el-button> -->
              <el-button size="mini" type="text" @click.native="gotoHome" style="padding-top:20px;font-size:100%;">首页</el-button>
            </el-col>

            <el-col :span="2">
                <!-- <el-button style="margin-top:10px; font-size:18px;" @click.native="gotoHotlist" type='text'>热榜</el-button> -->
                <el-button  type="text" @click.native="gotoHotlist" style="padding-top:20px;font-size:100%;">政策匹配</el-button>
            </el-col>
            <el-col :span="2" v-if="role==1">
                <!-- <el-button style="margin-top:10px; font-size:18px;" @click.native="gotoHotlist" type='text'>热榜</el-button> -->
                <el-button  type="text" @click.native="toManagePage" style="padding-top:20px;font-size:100%;">政策管理</el-button>
            </el-col>
            <el-col :span="4">
                <!-- <el-input size="small " style="padding-top:10px;" v-model="search" placeholder="请输入你想要进行的查询">
                    <el-button type="text" size="medium " @click.native="gotoSearch" slot="append" icon="el-icon-search" ></el-button>
                </el-input> -->
                <el-autocomplete style="width:150%;margin-top:14px;" size="small" v-model="search" :fetch-suggestions="querySearch" placeholder="请输入你想要进行的查询"
                                :trigger-on-focus="false" @select="handleSelect" @keyup.native="headerSearch">
                    <el-button style="width:50px;" @click.native="gotoSearch" slot="append" icon="el-icon-search" ></el-button>
                </el-autocomplete>
            </el-col>
            <el-col :span="0.5" :offset="5">
                <div class="btn-bell">
                    <el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">
                        <router-link to="/myNotice">
                            <i style="padding-top:25px;" class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="message > 0"></span>
                </div>
            </el-col>
            <el-col :span="2"><div class="user-avator">
                  <el-image  style="margin-top:11px;margin-left:40px;width:50%;height:50%;":src="require('../assets/img.jpg')" fit="cover"></el-image></div></el-col>
            <el-col :span="2">
                <el-dropdown @command="handleCommand" style="padding-top:15px;">
                    <span class="el-dropdown-link">{{username}}<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command='userpage' icon='el-icon-s-home'>我的主页</el-dropdown-item>
                        <el-dropdown-item command='modifyPass' icon='el-icon-s-tools'>修改密码</el-dropdown-item>
                        <el-dropdown-item command='logout' icon='el-icon-error'>退出登陆</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import questions from '../util/question'
import search from '@/util/search'
import api from '@/util/api'
import axios from 'axios'
const kindOptions = ['科学','数码','体育','时尚','影视','生活','游戏']
export default {
    name:'header',
    data(){
            return{
                role: 0,
                search:'',
                searchList:[],
                searchItemList: [],
                timeout:null,
                message:0,
                scores:this.$store.getters.getUserScore,
                imgUrl:this.$store.getters.getHeaderPictureURL,
                username:this.$store.getters.getUserName,
                queryForm: false,
                inputVisibleTag: false,
                inputValue:'',
                kinds:kindOptions,
                askForm:{
                    content: '',
                    dynamicTags: [],
                    dynamicKinds:['科学'],
                    reward:''
                },
                talkEnable: false
            }
    },
    mounted(){
        if(this.$store.getters.getHeaderPictureURL == "undefined")
          this.imgUrl = require('../assets/default_header.jpg')
        if(this.$store.getters.getIsComment == false)
            this.talkEnable = true
        else
            this.talkEnable = false
        this.searchList = [
            {"value":"印度阿三","Id":""},
            {"value":"古力娜扎","Id":""},
            {"value":"迪丽热巴","Id":""}]
        //判断角色
        if (this.$store.getters.getUserInfo || localStorage.getItem("role")) {
            this.role = this.$store.getters.getUserInfo.role || localStorage.getItem("role")
        }
        this.getUserMessage()
    },
    methods:{
        getUserMessage: function() {
            let _this = this
            axios.get(api.getUserMessage, {
                headers: {
                    userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
                }
            }).then(function(response) {
                if (response.data.data.unReadMessage)
                    _this.message = response.data.data.unReadMessage.length
            })
        },
        toManagePage: function() {
            this.$router.push("/manage/manageUser")
        },
        headerSearch: function(){
          // this.searchList = []
          // let _this = this
          // search.getSearchItem(this.search, function(searchList){
          //   _this.searchItemList = searchList
          //   _this.searchList = []
          //   searchList.forEach(search=>{
          //     let info = {
          //       value: search.content,
          //       id: ''
          //     }
          //     _this.searchList.push(info)
          //   })
          //   console.log(_this.searchList)
          // })
        },
        querySearch(queryString, cb){
            let searchList = this.searchList
            var results = queryString ? searchList.filter(this.createFilter(queryString)):searchList
            clearTimeout(this.timeout)
            this.timeout = setTimeout(()=>{
                cb(searchList)
            },1000 * Math.random());
        },
        createFilter(queryString){
            return (search)=>{
                return (search.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1)
            }
        },
        handleSelect(item){
            this.$store.commit('setPolicyId', item.Id)
            this.$router.push("/problemDetail")
        },
        handleCloseTag(tag){
            this.askForm.dynamicTags.splice(this.askForm.dynamicTags.indexOf(tag), 1);
        },
        showInputTag() {
            this.inputVisibleTag = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        handleInputConfirmTag() {
            let inputValue = this.inputValue;
            if (inputValue) {
                this.askForm.dynamicTags.push(inputValue);
            }
            this.inputVisibleTag = false;
            this.inputValue = '';
        },
        handleCommand(command){
            if(command==='userpage'){
                this.$router.push('/hostPage')
            }
            else if(command==='modifyPass'){
                this.$router.push('/modifyPass')
            }
            else if(command==='logout'){
                this.$router.push('/')
                this.$message({
                    showClose:true,
                    message: '退出成功',
                    type: 'success'
                });
            }
        },
        reset:function(){
            this.askForm.content='';
            this.askForm.dynamicTags=[];
            this.askForm.dynamicKinds=[];
            this.askForm.reward='';
        },
        commitQuestions: function(){
            // questions.commitQuestion(this.content, null, '00000001', 0)
            if(this.askForm.reward > this.$store.getters.getUserScore){
              this.$message({
                showClose:true,
                message:'悬赏积分超过当前拥有个数',
                type:'error'
              })
            }
            else{
              let label_m = this.askForm.dynamicTags.join(",")
              let complement = this.askForm.dynamicKinds.join(",")
              questions.commitQuestion(this.askForm.content, this.$store.getters.getUserID, this.askForm.reward, label_m, complement)
              this.$store.commit('minusUserScore', this.askForm.reward)
              this.scores = this.$store.getters.getUserScore
              this.reset()
              this.$message({
                showClose:true,
                message:'发布问题成功',
                type:'success'
              })
              this.queryForm = false
            }
        },
        gotoHome:function(){
            this.$router.push('/home')
        },
        gotoHotlist:function(){
            this.$router.push('/hotlist')
            // this.$message({
            //     showClose:true,
            //     message:'该功能暂未开放',
            //     type:'warning'
            // })
        },
        gotoSearch:function(){
            // this.$message({
            //     showClose:true,
            //     message:'该功能暂未开放',
            //     type:'warning'
            // })
            if(this.search){
                this.$store.commit('setSearchContent',this.search)

                this.$router.push('/search')
            }
            else{
                this.$message({
                    showClose:true,
                    message:'请输入想要查询的内容',
                    type:'warning'
                })
            }
        },
        getFoucus: function () {
            // this.content = null
        }
    },
    watch: {
      search: function(){
        let _this = this
        _this.searchList = []
        axios.get(api.searchPolicyByTitle, {
            params: {
                title: _this.search
            }
        }).then(function(response) {
            let policies = response.data
            console.log(_this.searchList)
            for (let i = 0; i < policies.length; i++) {
                let search = {
                    "Id": policies[i].id,
                    "value": policies[i].title
                }
                _this.searchList.push(search)
            }
        }).catch(function (err) {

        })
      }
    }
}
</script>

<style scoped>

    .header {
        background: #006EFF ;
        position: absolute;
        top: 0;
        left: 0;
        box-sizing: border-box;
        width: 100%;
        height: 60px;
        /* font-size: 28px; */
        color: #fff;
    }
    .selected{
        position: relative;;
        top:12px;
        color:#81BEF7;
    }
    .selected:hover {
        background-color: grey;
        border: 2px solid white;
        color: white;
    }
    .el-button {
        #background-color: #000;
        padding-top:10px;
        color:#fff
    }
    .el-button:hover {
        #background-color: #000;
        padding-top:10px;
        color:DeepSkyBlue
    }
    /* .el-row{
        padding-top:20px;
    } */
    /* .el-button{
        font-size: 24px;
        color:white;
    } */
    .query {
      font-size: 24px;
      color: black;
    }
    .btn-bell{
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }

    .btn-bell-badge{
        position: absolute;
        right: 0;
        top: 12px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }
     .btn-bell .el-icon-bell{
        color: #fff;
    }
    .user-avator{
        margin-left: 20px;
    }
    .user-avator img{
        display: block;
        width:50px;
        height:50px;
        border-radius: 50%;
    }
    .el-dropdown-link {
        cursor: pointer;
        color: white;
        font-size: 20px;
    }
    .el-icon-arrow-down {
        font-size: 20px;
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
