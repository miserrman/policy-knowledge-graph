import axios from 'axios'
export default {
  validate:  function (userName, password, callback) {
     var res = axios.get(  `/api/identify/vali?userName=${userName}&password=${password}`)
      .then(function (response) {
        if (response.data.mainData == null) {
         callback({
           'res': false,
           'data': response.data.tip
         })
        } else callback({
              'res': true,
              'data': response.data.mainData
        })
      })
      .catch(function (err) {
          console.log(err)
        }
      )
      return res
  },
  register: function(userName, password){
    axios.get(`/api/identify/sign/complete?userName=${userName}&password=${password}`)
      .then(function(response){
      })
      .catch(function(res){
      })
  },
  doubleNameAlert: function(userName){
    axios.get(`/api/identify/sign/validate?userName=${userName}`)
      .then(function(response){
        console.log(response)
        return response.data.mainData
      })
      .catch(function(err){
        console.log(err)
      })
  },
  modifyInfor: function(userID, userName, email){
    axios.get(`/api/identify/usermodify?userID=${userID}&userName=${userName}&email=${email}`)
      .then(function(response){
        console.log(response)
      })
      .catch(function(){
      })
  },
  getBanInformation: function(userID){
    axios.get(`api/identify/isban?userID=${userID}`)
      .then(function (response) {
        this.$store.commit("setIsComment", response.data)
      })
  },
  getPeopleDiscribe: function(userID, callback){
    axios.get(`api/identify/getpeoplediscribe?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  getLoginTime: function (userID, callback) {
    axios.get(`/api/identify/getlogintimes?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
      })
  },
  addLoginTime: function(userID){
    axios.get(`/api/identify/addlogintime?userID=${userID}`)
      .then(function(response){
        console.log(response.data)
      })
      .catch(function(err){})
  },
  getAchieve: function(userID, callback){
    axios.get(`/api/identify/getachieve?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){console.log(err)})
  },
  signOn: function(userID){
    axios.get(`/api/identify/signon?userID=${userID}`)
      .then(function(response){})
      .catch(function(err){console.log(err)})
  },
  modifyPassword: function(userID, password){
    axios.get(`/api/identify/modifypass?userID=${userID}&password=${password}`)
      .then(function(response){
      })
      .catch(function(err){})
  }
}
