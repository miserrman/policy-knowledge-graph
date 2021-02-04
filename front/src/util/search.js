import axios from 'axios'
export default{
    //获得搜索答案的问题列表
    getSearchItem: function (content, callback) {
      axios.get(`/api/search/dynamicsearch?content=${content}`)
        .then(function(response){
          callback(response.data)
        })
        .catch(function(err){
          console.log(err)
        })
    }
}
