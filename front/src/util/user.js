import axios from "axios";
export default {
  getUser: function(userName, callback){
    axios.get(`/api/identify/getuser?userName=${userName}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){})
  },
  getUserInfo: function (userID, callback) {
    axios.get(`api/answer/answeruserinfo?userID=${userID}`)
      .then(function (response) {
        callback(response.data)
      })
  },
  getOtherDiscribe(userID, callback) {
    axios.get(`api/identify/otherinfo?userID=${userID}`)
      .then(function (response) {
          callback(response.data)
        })
          .catch(function (err) {
            console.log(err)
          })

  },
  isWatchQuestion: function(questionID, userID, callback){
    axios.get(`api/query/iswatchquestion?questionID=${questionID}&userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  getUserChampion: function(userID, callback){
    axios.get(`/api/identify/userchampion?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  exmainePeopleInformation: function(userID, tel, header_picture, discribe){
    axios.get(`api/identify/setpeoplediscribe?userID=${userID}&tel=${tel}&header_picture=${header_picture}&discribe=${discribe}`)
      .then(function(response){
      })
      .catch(function(){})
  }
}

