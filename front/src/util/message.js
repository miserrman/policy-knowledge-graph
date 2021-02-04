import axios from 'axios'
export default {
  getAllMessages: function(userID, callback){
    axios.get(`/api/message/getallmessages?userID=${userID}`)
      .then(function (response) {
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  changeStatus: function(messageID, status){
    axios.get(`/api/message/changestatus?messageID=${messageID}&status=${status}`)
      .then(function(response){})
      .catch(function (err) {})
  },
  getUnreadMessageNum: function (userID, callback) {
    axios.get(`/api/message/getallmessages?userID=${userID}`)
      .then(function (response) {
        console.log(response.data)
        console.log("cdscscsdc")
        let msg = response.data.filter(data=>data.read_status == 0)
        callback(msg.length)
      })
      .catch(function(err){
        console.log(err)
      })
  }
}
