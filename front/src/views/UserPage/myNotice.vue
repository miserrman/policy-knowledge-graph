<template>
    <div>
        <!-- <el-card style="margin-top:30px;margin-left:100px;width:800px" v-for="message in messageList">
          <span>{{message.message_time}}</span>
            <p>{{message.content}}</p>
        </el-card> -->
        <el-tabs style="margin-top:20px;" v-model="activeName">
          <el-tab-pane :label="`未读消息(${unread.length})`" name="first">
              <el-table :data="unread" :show-header="false" style="width: 100%">
                  <el-table-column>
                      <template slot-scope="scope">
                          <span class="message-title">{{scope.row.title}}</span>
                      </template>
                  </el-table-column>
                  <el-table-column prop="date" width="180"></el-table-column>
                  <el-table-column width="120">
                      <template slot-scope="scope">
                          <el-button size="small" @click="handleRead(scope.$index)">标为已读</el-button>
                      </template>
                  </el-table-column>
              </el-table>
              <div class="handle-row">
                  <el-button type="primary" @click.native="setAllRead">全部标为已读</el-button>
              </div>
          </el-tab-pane>
          <el-tab-pane :label="`已读消息(${read.length})`" name="second">
              <template v-if="activeName === 'second'">
                  <el-table :data="read" :show-header="false" style="width: 100%">
                      <el-table-column>
                          <template slot-scope="scope">
                              <span class="message-title">{{scope.row.title}}</span>
                          </template>
                      </el-table-column>
                      <el-table-column prop="date" width="160"></el-table-column>
                      <el-table-column width="120">
                          <template slot-scope="scope">
                              <el-button type="danger" @click="handleDel(scope.$index)">删除</el-button>
                          </template>
                      </el-table-column>
                  </el-table>
                  <div class="handle-row">
                      <el-button type="danger" @click.native="delAllRead">删除全部</el-button>
                  </div>
              </template>
          </el-tab-pane>
          <el-tab-pane :label="`回收站(${recycle.length})`" name="third">
              <template v-if="activeName === 'third'">
                  <el-table :data="recycle" :show-header="false" style="width: 100%">
                      <el-table-column>
                          <template slot-scope="scope">
                              <span class="message-title">{{scope.row.title}}</span>
                          </template>
                      </el-table-column>
                      <el-table-column prop="date" width="160"></el-table-column>
                      <el-table-column width="120">
                          <template slot-scope="scope">
                              <el-button @click="handleRestore(scope.$index)">还原</el-button>
                          </template>
                      </el-table-column>
                  </el-table>
                  <div class="handle-row">
                      <el-button type="danger" @click.native="delStash">清空回收站</el-button>
                  </div>
              </template>
          </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
  import message from '@/util/message'
  import axios from 'axios'
  import api from '@/util/api'
export default {
    name:'myNotice',
    data(){
      return{
        messageList: [],
        activeName:'first',
        showHeader: false,
        unread: [{
            date: '2018-04-19 20:00:00',
            title: '【系统通知】该系统将于今晚凌晨2点到5点进行升级维护',
        },{
            date: '2018-04-19 21:00:00',
            title: '今晚12点整发大红包，先到先得',
        }],
        read: [{
            date: '2018-04-19 20:00:00',
            title: '【系统通知】该系统将于今晚凌晨2点到5点进行升级维护'
        }],
        recycle: [{
            date: '2018-04-19 20:00:00',
            title: '【系统通知】该系统将于今晚凌晨2点到5点进行升级维护'
        }]
      }
    },
    mounted(){
       this.loadMessagesList()
    },
    methods: {
      loadMessagesList: function(){
        let _this = this
        this.unread = []
        this.read = []
        this.recycle = []
        this.docu = []
        axios.get(api.getUserMessage, {
            headers: {
                userId: _this.$store.getters.getUserInfo.id || localStorage.getItem("userId")
            }
        }).then(function(response) {
            let data = response.data.data
            for (let i = 0; data.unReadMessage && i < data.unReadMessage.length; i++) {
                let info = {
                    title: data.unReadMessage[i].title,
                    date: data.unReadMessage[i].date
                }
                _this.unread.push(info)
            }
            for (let i = 0; data.readMessage && i < data.readMessage.length; i++) {
                let info = {
                    title: data.readMessage[i].title,
                    date: data.readMessage[i].date
                }
                _this.read.push(info)
            }
        })
      },
      handleRead(index) {
          const item = this.unread.splice(index, 1);
          let num = this.docu.indexOf(item[0])
          message.changeStatus(this.messageList[num].message_id, '1')
          this.$store.commit('setMessageNum', this.unread.length)
          this.read = item.concat(this.read);
      },
      handleDel(index) {
          const item = this.read.splice(index, 1);
          let num = this.docu.indexOf(item[0])
          message.changeStatus(this.messageList[num].message_id, '2')
          this.recycle = item.concat(this.recycle);
      },
      handleRestore(index) {
          const item = this.recycle.splice(index, 1);
          let num = this.docu.indexOf(item[0])
          message.changeStatus(this.messageList[num].message_id, '1')
          this.read = item.concat(this.read);
      },
      setAllRead:function(){
        const _this = this
        const item = this.unread
        this.unread.forEach(data=>{
          let num = _this.docu.indexOf(data)
          message.changeStatus(_this.messageList[num].message_id, '1')
        })
        this.read = item.concat(this.read)
        this.unread = []
        this.$store.commit('setMessageNum', 0)
      },
      delAllRead:function(){
        const _this = this
        const item = this.read
        this.read.forEach(data=>{
          let num = _this.docu.indexOf(data)
          message.changeStatus(_this.messageList[num].message_id, '2')
        })
        this.recycle = item.concat(this.recycle)
        this.read = []
      },
      delStash:function(){
        this.recycle = []
      }
    },
    computed: {
        unreadNum(){
            return this.unread.length;
        }
    }
}
</script>

<style scoped>
.message-title{
    cursor: pointer;
}
.handle-row{
    margin-top: 30px;
}
</style>
