const userID = 'UserID'
const userPassword = 'userPassword'
const userEmail = 'userEmail'
const userName = 'userName'
const tel = 'tel'
const questionID = 'questionID'
const question = 'question'
const isComment = 'isComment'
const userDiscibe = 'userDiscibe'
const otherUserID = 'otherUserID'
const policyId = 'policyId'

export default{
  setPolicyId: function(data) {
    window.sessionStorage.setItem(policyId, data)
  },
  getPolicyId: function () {
    return window.sessionStorage.getItem(policyId)
  },
  setUserID:function(data){
    window.sessionStorage.setItem(userID,data)
  },
  getUserID:function(){
    return window.sessionStorage.getItem(userID)
  },
  setPassword:function(data){
    window.sessionStorage.setItem(userPassword,data)
  },
  getUserPassword:function(){
    return window.sessionStorage.getItem(userPassword)
  },
  setUserEmail:function(data){
    window.sessionStorage.setItem(userEmail,data)
  },
  getUserEmail:function(){
    return window.sessionStorage.getItem(userEmail)
  },
  setUserName:function(data){
    window.sessionStorage.setItem(userName,data)
  },
  getUserName:function(){
    return window.sessionStorage.getItem(userName)
  },
  setTel:function(data){
    window.sessionStorage.setItem(tel,data)
  },
  getTel:function(){
    return window.sessionStorage.getItem(tel)
  },
  setQuestionID:function(data){
    window.sessionStorage.setItem(questionID,data)
  },
  getQuestionID:function(){
    return window.sessionStorage.getItem(questionID)
  },
  setQuestion:function(data){
    window.sessionStorage.setItem(question,data)
  },
  getQuestion:function(){
    return window.sessionStorage.getItem(question)
  },
  setIsComment:function(data){
    window.sessionStorage.setItem(isComment,data)
  },
  getIsComment:function(){
    return window.sessionStorage.getItem(isComment)
  },
  setUserDiscibe:function(data){
    window.sessionStorage.setItem(userDiscibe,data)
  },
  getUserDiscibe:function(){
    return window.sessionStorage.getItem(userDiscibe)
  },
  setOtherUserID:function(data){
    window.sessionStorage.setItem(otherUserID,data)
  },
  getOtherUserID:function(){
    return window.sessionStorage.getItem(otherUserID)
  }
}
