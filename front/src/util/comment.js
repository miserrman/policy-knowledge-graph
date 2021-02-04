import axios from 'axios'
export default {
  getAllComment: function(answerID, callback){
    axios.get(`/api/comment/getcommentlist?answerID=${answerID}`)
      .then(function(response){
        callback(response.data)
      })
  },
  commentAnswer: function(answerID, userID, comment, callback){
    axios.get(`/api/comment/commentanswer?answerID=${answerID}&userID=${userID}&comment=${comment}`)
      .then(function(response){
        callback(response.data)
      })
      .catch(function(err){
      })
  }
}
