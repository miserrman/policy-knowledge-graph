import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    userID: '',
    userPassword: '',
    userEmail: '',
    userName: '',
    tel: '',
    questionID: '',
    question: '',
    isComment: true,
    userDiscribe: '',
    otherUserID: '',
    headerPictureURL: '',
    searchContent:'',
    filterContent:'',
    filterKind:'',
    filterTag:'',
    isArrowUp:'',
    userScore: 0,
    loginTime: 0,
    messageNum: 0,
    isSign: false
  },
  getters: {
    getMessageNum: function(state){
      if(!state.messageNum)
        state.messageNum = window.sessionStorage.getItem('messageNum')
      return state.messageNum
    },
    getIsSign: function(state){
      if(!state.isSign)
        state.isSign = window.sessionStorage.getItem('isSign')
      return state.isSign
    },
    getLoginTime: function(state){
      if(!state.loginTime)
        state.loginTime = window.sessionStorage.getItem('loginTime')
      return state.loginTime
    },
    getUserScore: function(state){
      if(!state.userScore){
        state.userScore = window.sessionStorage.getItem('userScore')
      }
      return state.userScore
    },

    getUserID: function (state) {
      if(!state.userID){
        state.userID = window.sessionStorage.getItem('userID')
      }
      return state.userID
    },
    getUserName: function(state){
      if(!state.userName){
        state.userName = window.sessionStorage.getItem('userName')
      }
      return state.userName
    },
    getUserPassword: function (state) {
      if(!state.userPassword){
        state.userPassword = window.sessionStorage.getItem('userPassword')
      }
      return state.userPassword
    },
    getUserEmail: function(state){
      if(!state.userEmail){
        state.userEmail = window.sessionStorage.getItem('userEmail')
      }
      return state.userEmail
    },
    getUserTel: function(state){
      if(!state.tel){
        state.tel = window.sessionStorage.getItem('tel')
      }
      return state.tel
    },
    getQuestionID: function(state){
      if(!state.questionID){
        state.questionID = window.sessionStorage.getItem('questionID')
      }
      return state.questionID
    },
    getQuestion: function(state){
      if(!state.question){
        state.question = window.sessionStorage.getItem('question')
      }
      return state.question
    },
    getIsComment: function(state){
      if(!state.isComment){
        state.isComment = window.sessionStorage.getItem('isComment')
      }
      return state.question
    },
    getUserDiscribe: function(state){
      if(!state.userDiscibe){
        state.userDiscibe = window.sessionStorage.getItem('userDiscibe')
      }
      return state.userDiscibe
    },
    getOtherUserID: function(state){
      if(!state.otherUserID){
        state.otherUserID = window.sessionStorage.getItem('otherUserID')
      }
      return state.otherUserID
    },
    getHeaderPictureURL: function(state){
      if(!state.headerPictureURL){
        state.headerPictureURL = window.sessionStorage.getItem('headerPictureURL')
      }
      return state.headerPictureURL
    },
    getSearchContent:function(state){
      if(!state.searchContent){
        state.searchContent = window.sessionStorage.getItem('searchContent')
      }
      return state.searchContent
    },
    getFilterContent:function(state){
      if(!state.filterContent){
        state.filterContent = window.sessionStorage.getItem('filterContent')
      }
      return state.filterContent
    },
    getFilterKind:function(state){
      if(!state.filterKind){
        state.filterKind = window.sessionStorage.getItem('filterKind')
      }
      return state.filterKind
    },
    getFilterTag:function(state){
      if(!filterTag){
        state.filterTag = window.sessionStorage.getItem('filterTag')
      }
      return state.filterKind
    },
    getIsArrowUp:function(state){
      if(!isArrowUp){
        state.isArrowUp = window.sessionStorage.getItem('isArrowUp')
      }
      return state.isArrowUp
    }
  },
  mutations: {
    setMessageNum: function(state, messageNum){
      window.sessionStorage.setItem('messageNum', state.messageNum)
      state.messageNum = messageNum
    },
    setLoginTime: function(state, loginTime){
      window.sessionStorage.setItem('loginTime', state.loginTime)
      state.loginTime = loginTime
    },
    addUserScore(state, score){
      window.sessionStorage.setItem('userScore', state.userScore + score)
      state.userScore = score + state.userScore
    },
    minusUserScore(state, score){
      window.sessionStorage.setItem('userScore', state.userScore - score)
      state.userScore = state.userScore - score
    },
    setUserID(state, ID){
      window.sessionStorage.setItem('userID',ID)
      state.userID = ID
    },
    setUserPassword(state, password){
      window.sessionStorage.setItem('userPassword',password)
      state.userPassword = password
    },
    setUserEmail(state, email){
      window.sessionStorage.setItem('userEmail',email)
      state.userEmail = email
    },
    setUserName(state, name){
      window.sessionStorage.setItem('userName',name)
      state.userName = name
    },
    setUserTel(state, tel){
      window.sessionStorage.setItem('tel',tel)
      state.tel = tel
    },
    setQuestionID(state, questionID){
      window.sessionStorage.setItem('questionID',questionID)
      state.questionID = questionID
    },
    setQuestion(state, question){
      window.sessionStorage.setItem('question',question)
      state.question = question
    },
    setIsComment(state, isComment){
      window.sessionStorage.setItem('isComment',isComment)
      state.isComment = isComment
    },
    setUserDiscribe(state, userDiscribe){
      window.sessionStorage.setItem('userDiscibe',userDiscribe)
      state.userDiscribe = userDiscribe
    },
    setOtherUserID: function(state, otherUserID){
      window.sessionStorage.setItem('otherUserID',otherUserID)
      state.otherUserID = otherUserID
    },
    setHeaderPictureURL: function(state, headerPictureURL){
      window.sessionStorage.setItem('headerPictureURL',headerPictureURL)
      state.headerPictureURL = headerPictureURL
    },
    setSearchContent:function(state,searchContent){
      window.sessionStorage.setItem('searchContent', searchContent)
      state.searchContent = searchContent
    },
    setFilterContent:function(state,filterContent){
      window.sessionStorage.setItem('filterContent',filterContent)
      state.filterContent = filterContent
    },
    setUserScore: function(state, score){
      window.sessionStorage.setItem('userScore', score)
      state.userScore = score
    },
    setFilterKind:function(state,filterKind){
      window.sessionStorage.setItem('filterKind',filterKind)
      state.filterKind = filterKind
    },
    setFilterTag:function(state,filterTag){
      window.sessionStorage.setItem('filterTag',filterTag)
      state.filterTag = filterTag
    },
    setIsArrowUp:function(state,filterTag){
      window.sessionStorage.setItem('isArrowUp',isArrowUp)
      state.isArrowUp = isArrowUp
    },
    setIsSign: function(state, isSign){
      window.sessionStorage.setItem('isSign', isSign)
      state.isSign = isSign
    }
  }
})

export default store
