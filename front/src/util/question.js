import axios from 'axios'
import answer from '../util/answer'
import user from '../util/user'

export default{
  watchTheQuestion: function(questionID, userID, judge){
    axios.get(`api/query/watchthequestion?questionID=${questionID}&userID=${userID}&judge=${judge.toString()}`)
      .then(function(reponse){})
      .catch(function(err){})
  },
  getTheQuestion: function(questionID, callback){
    axios.get(`api/query/getthequestion?questionID=${questionID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
      })
  },
  commitQuestion: function(question, userID, score, label_m, complement){
    axios.get(`/api/query/commit?question=${question}&userID=${userID}&q_score=${score}&label_m=${label_m}&complement=${complement}`)
      .then(function (response) {
      })
      .catch(function (err) {
      })
  },
  getAllQuestion: function(questionList, userID){
    axios.get(`/api/query/getallquestions`)
      .then(function(response){
        response.data.forEach(data=>{
              answer.getAllAnswers(data.question_id,function(result){
                let ans = result[0]
                let res = ans.answer.match(/<p>[\u4e00-\u9fa5]+|\w+<\/p>/)
                if(res != null)
                  data.suggestion = res[0] + '<\/p>'
                else
                  data.suggestion = res
                user.isWatchQuestion(data.question_id, userID, function (watch) {
                  let r = {
                    question: data,
                    isWatch: watch
                  }
                  questionList.push(r)
                })
          })
        })
      })
      .catch(function(err){
      })

  },
  getFollowedQuestion: function(userID, callback){
    axios.get(`/api/query/getfollowedquestions?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){})
  },
  getHighScoreQuestions: function(callback){
    axios.get(`/api/query/gethighscore`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){})
  },
  fetchAnswers: function(comment) {
    let res = ''
    let content = comment.match(regex)
    content.forEach(data => {
        res += data.toString()
      }
    )
    return res
  },
  getKindQuestion: function(kind, callback){
    axios.get(`api/query/getkindquestion?kind=${kind}`)
      .then(function(response){
        callback(response.data)
    })
      .catch(function(err){
      })
  },
  scoreQuestion: function (userID, callback) {
    axios.get(`api/query/getscorequestion?userID=${userID}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
        console.log(err)
      })
  },
  questionTimeCal: function (questionTime) {
    const currentTime = new Date()
    const subres = currentTime - Date.parse(questionTime)
    if(subres < 1000 * 60){
      const num = Math.floor(subres / 1000).toString()
      return '刚刚发布'
    }
    else if(subres < 1000 * 60 * 60){
      const num = Math.floor(subres / (60 * 1000)).toString()
      return '您于' + num + '分钟前发布的'
    }
    else if(subres < 1000 * 60 * 60 * 24){
      const num = Math.floor(subres / (1000 * 60 * 60)).toString()
      return '您于' + num + '小时前发布的'
    }
    else{
      questionTime += 8 * 60 * 60 * 1000
      let d = questionTime.toString()
      let arr_d = d.split(' ')
      const res = arr_d[1] + " " + arr_d[2] + " " + arr_d[3] +  arr_d[4];
      return res + ' 发布'
    }
  }
}
