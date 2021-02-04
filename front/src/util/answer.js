import axios from 'axios'
import question from "@/util/question";

export default {
  storeTheAnswer: function(answerID, userID, res){
    console.log(answerID)
    console.log(userID)
    console.log("cdscdcs")
    axios.get(`api/answer/storetheanswer?answerID=${answerID}&userID=${userID}&res=${res.toString()}`)
      .then(function(response){})
      .catch(function(err){})
  },
  isStore: function(answerID, userID, callback){
    axios.get(`api/answer/isstore?answerID=${answerID}&userID=${userID}`)
      .then(function(response){
        callback(response.data)}
        )
  },
  storeTheQuestion: function(answerID, userID, res){
    axios.get(`api/answer/storetheanswer?answerID=${answerID}&userID=${userID}`)
      .then(function(response){})
      .catch(function(err){})
},
  getAllAnswers: function(questionID, callback){
    axios.get(`api/answer/findanswer?questionId=${questionID}`)
      .then(function (response) {
        callback(response.data)
      })
      .catch(function(err){
      })
  },
  getAnswer: function(answerID, callback){
    axios.get(`/api/answer/gettheanswer?answerID=${answerID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  commitAnswer: function(userID, questionID, content){
    axios.get(`/api/answer/commitanswer?userID=${userID}&questionID=${questionID}&html_text=${content}`)
      .then(function(response){})
      .catch(function(err){console.log(err)})
  },
  comm: function(list){
    list.push("sdccds")
  },
  getUserInfo: function(answer, callback){
    axios.get(`api/answer/answeruserinfo?userID=${answer.answer_user}`)
      .then(function(response){
        callback(response.data)
      })
  },
  support: function(answerID, userID, isSupport){
    axios.get(`/api/answer/support?answerID=${answerID}&userID=${userID}&isSupport=${isSupport}`)
      .then(function(response){})
      .catch(function(err){})
    axios
  },
  getSupport: function(userID, answerID, callback){
    axios.get(`/api/answer/getsupport?answerID=${answerID}&userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  getCollectList: function(userID, callback){
    axios.get(`/api/answer/getcollectlist?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
      })
  },
  getAnswerScore: function(answerID, callback){
    axios.get(`/api/answer/getanswerscore?answerID=${answerID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  }
}
