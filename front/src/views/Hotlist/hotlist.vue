<!--热榜组件-->
<!--传入问题所有信息-->
<template>
  <div class="hotTemp">
    <el-dialog
              title="订阅信息"
              :visible.sync="dialogVisible"
              width="55%"
              :before-close="handleClose"
              :append-to-body="dialogVisible">
              <el-form style="margin-left:30px">
                <el-form-item label="政策部门">
                    <el-select placeholder="选择层级" v-model="form.depart.level" style="width:100px;margin-right:20px" size="mini" @change="changeCountry">
                      <el-option label="国家级" value="0"></el-option>
                      <el-option label="省市级" value="1"></el-option>
                      <el-option label="地区级" value="2"></el-option>
                    </el-select>
                    <el-select :disabled="departDisabled" placeholder="选择地区" size="mini" v-model="form.depart.region" style="width:100px;margin-right:20px" @change="changeRegion">
                      <el-option v-for="(region, index) in regionList" :label="regionList[index]" :value="regionList[index]"></el-option>
                    </el-select>
                    <el-select placeholder="选择部门" size="mini" v-model="form.depart.name" style="width:200px">
                      <el-option v-for="(depart, index) in departList" :label="departList[index]" :value="departList[index]"></el-option>
                    </el-select>
                    <el-button size="mini" style="margin-left:10px" @click="addDepartTag">添加</el-button>
                    <br/>
                    
                    <el-tag
                        :key="tag"
                        v-for="tag in departResultList"
                        closable  
                        :disable-transitions="false"
                        @close="handleCloseTagDepart(tag)" size="small">
                        {{tag.name}}
                    </el-tag>
                </el-form-item>
                
                <el-form-item label="覆盖范围">
                  <el-checkbox-group v-model="form.coverArea">
                    <el-checkbox label="福建省" name="coverArea"></el-checkbox>
                    <el-checkbox label="广东省" name="coverArea"></el-checkbox>
                  </el-checkbox-group>  
                </el-form-item>
                <el-form-item label="经营单位">
                    <el-select placeholder="单位类别" v-model="form.organization.name" size="mini" style="width:100px;margin-right:20px">
                      <el-option v-for="(company, index) in companyList" :label="companyList[index]" :value="companyList[index]">
                      </el-option>
                    </el-select>
                    <el-select placeholder="单位规模" v-model="form.organization.scale" size="mini" style="width:200px">
                      <el-option label="民营中小企业" value="中小企业"></el-option>
                      <el-option label="民营大型企业" value="大型企业"></el-option>
                      <el-option label="国有企业" value="国有企业"></el-option>
                    </el-select>
                </el-form-item> 
                <el-form-item label="企业行业">
                  <el-checkbox-group v-model="form.industry">
                    <el-checkbox label="计算机" name="industry"></el-checkbox>
                    <el-checkbox label="工业" name="industry"></el-checkbox>
                    <el-checkbox label="教育" name="industry"></el-checkbox>
                    <el-checkbox label="金融" name="industry"></el-checkbox>
                    <el-checkbox label="培训机构" name="industry"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="添加关键词">
                    <el-tag
                        :key="tag"
                        v-for="tag in form.keyword.keywordList"
                        closable
                        :disable-transitions="false"
                        @close="handleCloseTag(tag)">
                        {{tag}}
                    </el-tag>
                    <el-input
                      class="input-new-tag"
                      v-if="form.keyword.inputVisible"
                      v-model="form.keyword.inputValue"
                      ref="saveTagInput"
                      size="small"
                      @keyup.enter.native="handleInputConfirmTag"
                      @blur="handleInputConfirmTag">
                    </el-input>
                    <el-button v-else class="button-new-tag" size="small" @click="showInputTag">+ New Tag</el-button>
                </el-form-item>
                <el-form-item label="匹配方式">
                    <el-radio-group v-model="form.match" size="medium">
                      <el-radio border label="精准匹配" style="margin-left:20px"></el-radio>
                      <el-radio border label="广泛匹配"></el-radio>
                    </el-radio-group>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveSubscribeDialog">保 存</el-button>
              </span>
  </el-dialog>
    <el-header><Header></Header></el-header>
    <div id="knowledgeGraphShow" class="graph-container">
        
    </div>
    <div class="recommend">
      <!-- <el-tag></el-tag>
      <span>我的匹配</span>
      <policy-card></policy-card> -->
      <img src="../../assets/caption.jpg" style="float:left;width:40px;height:40px;margin-right:20px;position:relative;top:15px">
      <h2>
        智能专区
      </h2>
      <el-card style="width:450px;float:left;margin:20px;">
        <i class="el-icon-s-management"></i><span class="title-span">我的订阅</span>
        <el-button style="float:right" type="primary" round size="mini" @click="dialogInit">管理订阅</el-button>
        <br/>
        <br/>
        <span v-if="subscribePolicy.length <= 0" style="font-size:14px;font-style:oblique"><br/>还未订阅哦，快来订阅你的专属政策吧!<br/></span>
        <div v-for="(policy, index) in subscribePolicy">
          <div style="float:left"><i class="el-icon-star-off"></i></div>
          <div class="subscribe-title-span"><span @click="toSubscribeDetail(index)">{{policy.title}}</span>
          <br/>
          <br/>
        </div>
      </div>

      </el-card>
      <el-card style="width:450px;float:left;margin:20px">
        <i class="el-icon-s-opportunity"></i><span class="title-span">我的匹配</span>
        <br/>
        <br/>
        <span v-if="recommendPolicyTitles.length <= 0" style="font-size:14px;font-style:oblique"><br/>还加入公司哦，快来加入公司吧!<br/></span>
        <div v-for="(policy, index) in recommendPolicyTitles">
          <div style="float:left"><i class="el-icon-star-off"></i></div>
          <div class="subscribe-title-span"><span @click="toRecommendDetail(index)">{{policy.title}}</span>
          <br/>
          <br/>
        </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
    let echarts = require("echarts")
    import PolicyCard from '@/components/policy_card'
    import api from '@/util/api'
    import axios from 'axios'
    import Header from '@/components/header'
    import question from '@/util/question'
    import ElHeader from "element-ui/packages/header/src/main";
    import ElContainer from "element-ui/packages/container/src/main";
    export default {
        name: "hotlist",
        components:{
          ElContainer,
          ElHeader,
          Header,
          PolicyCard
        },
        data() {
          return {
            recommendPolicyTitles: [],
            subscribePolicy: [],
            departDisabled: false,
            regionList: ["福建省","广东省","深圳市"], //地区有哪些
            departList: ["福建共青团"],//部门有哪些
            companyList: ["学校","基金会","工业基地","软件园","工业园","培训机构","国有企业"],
            dialogVisible: false,
            hotQuestionList: [],
            graphEntity: [],
            graphRelation: [],
            labels: [],
            departResultList: [],
            form: {
              depart: {
                level: "",
                region: '',
                name: ''
              },
              coverArea: [],
              organization: {
                name: "",
                scale: "",
              },
              industry: [],
              keyword: {
                keywordList: [],
                inputVisible: false,
                inputValue: ''
              },
              match: "精准匹配"
            },
            allDepartData: [],
            allRegionData: []
          }
        },
        mounted(){
          this.getPolicyGraph()
          this.getSubscibePolicy()
          this.getRecommendPolicy()
        },
        methods: {
          toRecommendDetail: function(index) {
            this.$store.commit('setPolicyId', this.recommendPolicyTitles[index].id)
            this.$router.push("/problemDetail")
          },
          getRecommendPolicy: function() {
            this.recommendPolicyTitles = []
            let _this = this
            axios.get(api.getRecommendPolicyTitles,{
              headers: {
                userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
              }
            }).then(function(response) {
              _this.recommendPolicyTitles = response.data.data
            })
          },
          saveSubscribeDialog: function() {
            let _this = this
            let match = 0
            if (this.form.match == "广泛匹配") 
              match = 1;
            // axios.post(api.saveSubscribeForm, {
            //   headers: {
            //     userId: _this.$store.getters.getUserInfo.id 
            //   },
            //   data: {
            //     departIds: _this.departResultList.join(),
            //     regions: _this.form.coverArea.join(),
            //     organization: _this.form.organization.name,
            //     scale: _this.form.organization.scale,
            //     industry: _this.form.industry.join(),
            //     keywords: _this.form.keyword.keywordList.join(),
            //     mate: match
            //   }
            // }).then(function(response) {
            //   _this.dialogVisible = false
            //   console.log(response)
            // })
            let departIdResult = []
            for (let i = 0; i < this.departResultList.length; i++)
              departIdResult.push(this.departResultList[i].id)
            let scale = null
            if (this.form.organization.scale == "中小企业")
              scale = 0
            else if (this.form.organization.scale == "大型企业")
              scale = 1
            else if (this.form.organization.scale == "国有企业")
              scale = 2
            axios({
              method: 'post',
              url: api.saveSubscribeForm,
              data: {
                departIds: departIdResult.join(),
                // regions: _this.form.coverArea.join(),
                organization: _this.form.organization.name,
                scale: scale,
                industry: _this.form.industry.join(),
                keywords: _this.form.keyword.keywordList.join(),
                mate: match
              },
              headers: {
                userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
              }
            }).then(function(response) {
              _this.dialogVisible = false
              _this.getSubscibePolicy()
            })
          },
          addDepartTag: function() {
            if (this.form.depart.name != "") {
              for (let i = 0; i < this.departResultList.length; i++) {
                if (this.form.depart.name == this.departResultList[i].name)
                  return
              }
              for (let i = 0; i < this.allDepartData.length; i++) {
                if (this.form.depart.name == this.allDepartData[i].name) {
                  this.departResultList.push(this.allDepartData[i])
                  break
                }
              }
            }
          },
          toSubscribeDetail: function(index) {
            this.$store.commit('setPolicyId', this.subscribePolicy[index].id)
            this.$router.push("/problemDetail")
          },
          getSubscibePolicy: function() {
            let _this = this
            axios.get(api.getSubscribePolicy, {
              headers: {
                userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
              }
            }).then(function(response) {
               if (response.data.data != null) 
                _this.subscribePolicy = response.data.data
            })
          },
          changeCountry: function() {
            this.departList = []
            if (this.form.depart.level == 0) {
              this.departDisabled = true
              for (let i = 0; i < this.allDpartData.length; i++) {
                if (this.allDpartData[i].level == 0)
                  this.departList.push(this.allDepartData[i].name)
              }
            }
            else if (this.form.depart.level == 1) {
              this.regionList = []
              this.departDisabled = false
              for (let i = 0; i < this.allRegionData.length; i++) {
                this.regionList = this.allRegionData
              }
              this.regionList.push("广东省")
            }
          },
          changeRegion: function() {
            this.departList = []
            for (let i = 0; i < this.allDepartData.length; i++) {
              if (this.allDepartData[i].region == this.form.depart.region)
                this.departList.push(this.allDepartData[i].name)
            }
            console.log(this.allDepartData)
          },
          dialogInit: function() {
            let _this = this
            axios.get(api.initSubscribeDialog, {
                headers: {
                  'userId': _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
                }
              }).then(function(response) {
                  let data = response.data.data
                  _this.allDepartData = data.policyDepartList
                  _this.allRegionData = data.regionList
                  if (data.subscribe != null) {
                      let departIdList = data.subscribe.departIds
                      departIdList = departIdList.toString().split(",")
                      _this.departResultList = []
                      for (let i = 0; i < departIdList.length; i++) 
                      for (let j = 0; j < data.policyDepartList.length; j++) {
                        if (departIdList[i] == data.policyDepartList[j].id)
                          _this.departResultList.push(data.policyDepartList[j])
                      }
                      let regionList = []
                      if (data.subscribe.regions) 
                         regionList = data.subscribe.regions.toString().split(",")
                      if (data.subscribe.scale) {
                        if (data.subscribe.scale == 0) {
                          _this.form.organization.scale = "中小企业"
                        } else if (data.subscribe.scale == 1) {
                          _this.form.organization.scale = "大型企业"
                        } else {
                          _this.form.organization.scale = "国有企业"
                        }
                      }
                      _this.form.coverArea = regionList
                      _this.form.organization.name = data.subscribe.organization
                      let industry = data.subscribe.industry.toString().split(",")
                      _this.form.industry = industry
                      _this.form.keyword.keywordList = data.subscribe.keywords.toString().split(",")
                      if (data.subscribe.mate == 0) {
                        _this.form.match = "精准匹配"
                      }
                      else {
                        _this.form.match = "广泛匹配"
                      }
                  }
                  _this.dialogVisible = true
              })
          },
          handleCloseTag(tag) {
            this.form.keyword.keywordList.splice(this.form.keyword.keywordList.indexOf(tag), 1);
          },

          handleCloseTagDepart(tag) {
            this.departResultList.splice(this.departResultList.indexOf(tag), 1);
          },
          showInputTag:function() {
              this.form.keyword.inputVisible = true;
          },

        handleInputConfirmTag() {
          let inputValue = this.form.keyword.inputValue;
          if (inputValue) {
            this.form.keyword.keywordList.push(inputValue);
          }
          this.form.keyword.inputVisible = false;
          this.form.keyword.inputValue = '';
        },
          handleClose(done) {
            this.$confirm('确认关闭？')
            .then(_ => {
              console.log(this.form.coverArea)
              done();
            })
            .catch(_ => {});
            
          },
          manageSub: function() {
            this.manageSubscribe = true
            console.log(this.manageSubscribe)
          },
          loadKnowledgeGraph: function() {
            let myChart = echarts.init(document.getElementById("knowledgeGraphShow"))
             myChart.setOption({
            tooltip: {},
            itemStyle: {
              color: '#fffcf9'
            },
            series: [{
              type: 'graph',
              layout: 'force',
              force: {
                  repulsion: 130,
                  edgeLength: 10
              },
              roam: 'scale',
              label: {
                  show: true,
                  color: 'auto',
                  fontSize: 14
              },
              data: this.graphEntity,
              categories: this.labels
            }]
          })
        },
          getPolicyGraph: function() {
            let colorList = [
            '#CF4645','#B580B2','#29008A','#146A90','#8956FF','#70C9A8',
            '#bfbfbf','#595959',
            '#40a9ff','#1890ff',
            '#ffd666','#ffc53d','#ffc53d','#ffc069','#ffa940','#fa8c16',
            '#eccbd9','#ffadad','#ff6392','#ffc09f','#ffcb77','#ffe066','#ffd53e','#ffda3d','#adf7b6','#a0e8af','#80ed99','#07beb8','#17c3b2','#48cae4','#97d2fb','#83bcff','#91e5f6','#9381ff']
            let fontSizeList = [
                12,12.5,13,13.5,14,14.5,
                15,15.5,16,16.5,17,17.5,
                18,18.5,19,19.5,20,20.5,
                21,22,23,
                24]
            let _this = this
            _this.graphEntity = []
            _this.graphRelation = []
            axios.get(api.getPolicyGraph).then(function(response) {
              let data = response.data.data
              let entity = data.entity
              let relation = data.relation
              _this.labels = []
              let label_t = []
              for (let i = 0; i < entity.length; i++) {
                _this.labels.push({name: entity[i].label})
                label_t.push(entity[i].label)
              }
              _this.labels = Array.from(new Set(_this.labels))
              for (let i = 0; i < entity.length; i++) {
                _this.graphEntity.push({
                  name: entity[i].name,
                  value: 500,
                  draggable: true,
                  itemStyle: {
                    color: '#fffcf9'
                  },
                  label: {
                      color: colorList[Math.floor(Math.random()* colorList.length)],
                      fontSize: fontSizeList[Math.floor(Math.random()*colorList.length)]
                  }
                  // des: entity[i].name,
                  // symbolSize: 70,
                  // category: label_t.indexOf(entity[i].label)
                })
              }
              for (let i = 0; i < relation.length; i++) {
                _this.graphRelation.push({
                  source: relation[i].policyEntityStart.name,
                  target: relation[i].getPolicyEntityEnd.name,
                  name: "",
                  des: ''
                })
              }
              _this.loadKnowledgeGraph()
              console.log(_this.graphEntity)
            })
          },
          createGraph: function() {

          },
          gotoDetail: function(index){
            let questionID = this.hotQuestionList[index].question_id
            this.$store.commit('setQuestionID', questionID)
            this.$router.push('/problemDetail')
          },
          loadHotQuestionList: function(){
            const _this = this
            question.getHighScoreQuestions(function(questionList){
              _this.hotQuestionList = questionList.slice()
              console.log(_this.hotQuestionList)
            })
          },
          /**
           * 获取问题热榜信息
           */
          getQuestionHotRanking: function (questionID) {
            
          },
          getPhotoURL: function (questionID) {

          }
        }
    }
</script>

<style scoped>
  .graph-container {
    width: 100%;
    height: 300px;
    position: relative;
    bottom: 5px;
    background-repeat: no-repeat;
    background-image: url(../../assets/background1.jpg);
    background-size: 100%;
    box-shadow:lightgray 5px 5px 30px 5px ;
  }

  .el-container{
    width: 100%;
    height: 100%;
  }
  .el-aside{
    max-width: 20px;
    width: 5%;
    text-align: center;
    padding-top: 3%;
  }
  .el-aside p{

  }
  .el-header{
  }
  .el-main{
    height: 100%;
    padding: 0px;
    margin: 0;
    border: 0;
    margin: 0;
  }
  .main-content p{
    margin: 12px;
    font-weight: bolder;
    font-size: 16px;
  }
  .main-content .con{
    margin: 12px;
    font-size: 12px;
    overflow: hidden;
    word-wrap: break-word;
    height: 35%;
  }
  .main-content div{
    margin: 12px;
  }
  .main-content{
    padding-top: 3%;
    width: 68%;
    height: 100%;
    float: left;
    padding: 0;
  }
  .main-content p{
    height: 10px;
  }
  .image-content img{
    width: 100%;
    height: 100%;
  }
  .image-content{
    width: 32%;
    float: left;
    height: 100%;
  }
  .recommend {
    padding-top: 40px;
    width: 1000px;
    margin: 0 auto;
  }
  .policy-card {
    width: 400px;
  }
  .title-span {
    margin-left: 20px;
  }
  .subscribe-title-span {
    margin-left: 20px;
    font-size: 14px;
    float: left;
    width: 90%;
  }
  .subscribe-title-span :hover {
    cursor: pointer;
    color: rgb(87, 87, 253);
    text-decoration: underline;
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
