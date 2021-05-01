import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login'
import register from '@/views/register'
import retrievePassword from '@/views/retrievePassword'
import password from '@/views/password'
import home from '@/views/Home/home'
import search from '@/views/Search/search'
import all from '@/views/Home/kinds/all'
import science from '@/views/Home/kinds/science'
import economy from '@/views/Home/kinds/economy'
import tour from '@/views/Home/kinds/tour'
import environment from '@/views/Home/kinds/environment'
import info from '@/views/Home/kinds/info'
import society from '@/views/Home/kinds/society'
import otherUserAsk from '@/views/OtherUser/page/OtherQuestion'
import otherUserStore from '@/views/OtherUser/page/OtherStore'
// import follow from '@/components/follow'
// import stars from '@/views/Home/stars'
import hotlist from '@/views/Hotlist/hotlist'
import userpage from '@/views/UserPage/userpage'
import userinfo from '@/views/UserPage/userinfo'
import myAsk from '@/views/UserPage/myAsk'
import myCollect from '@/views/UserPage/myCollect'
import myFollow from '@/views/UserPage/myFollow'
import myNotice from '@/views/UserPage/myNotice'
import modifyPass from '@/views/UserPage/modifyPass'
import problemDetail from '@/views/problemDetail/problemDetail'
import writeAns from '@/views/markdown/writeAns'
import hostPage from '@/views/UserPage/hostPage'
import otherUserShow from '@/views/OtherUser/OtheUserShow'
import preLoad from '@/views/preLoad'
import notFound from '@/views/404'
import manage from '@/views/Manage/home'
import managePolicy from '@/views/Manage/managePolicy'
import manageUser from '@/views/Manage/manageUser'
import manageWrite from '@/views/Manage/manageWrite'

Vue.use(Router)


export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },{
      path:'/register',
      name:'register',
      component:register
    },{
      path:'/home',
      name:'home',
      component:home,
      children:[
        {
          path:'/',
          name:'全部',
          component:all
        }
      ]
    },{
      path: '/login',
      name: 'login',
      component: login
    },{
      path:'/home',
      name:'全部',
      component:home,
      children:[
        {
          path:'/all',
          name:'全部',
          component:all
        }
      ]
    },{
      path:'/home',
      name:'经济',
      component:home,
      children:[
        {
          path:'/economy',
          name:'经济',
          component:economy
        }
      ]
    },{
      path:'/home',
      name:'科教',
      component:home,
      children:[
        {
          path:'/science',
          name:'科教',
          component:science
        }
      ]
    },{
      path:'/home',
      name:'环境',
      component:home,
      children:[
        {
          path:'/environment',
          name:'环境',
          component:environment
        }
      ]
    },{
      path:'/home',
      name:'文旅',
      component:home,
      children:[
        {
          path:'/tour',
          name:'文旅',
          component:tour
        }
      ]
    },{
      path:'/home',
      name:'社会',
      component:home,
      children:[
        {
          path:'/society',
          name:'社会',
          component:society
        }
      ]
    },{
      path:'/home',
      name:'信息',
      component:home,
      children:[
        {
          path:'/info',
          name:'信息',
          component:info
        }
      ]
    },{
      path:'/hotlist',
      name:'热榜',
      component:hotlist
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/',
          name:'个人信息',
          component:userinfo
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/userinfo',
          name:'个人信息',
          component:userinfo
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/myAsk',
          name:'公司信息',
          component:myAsk
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/myCollect',
          name:'我的收藏',
          component:myCollect
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/myFollow',
          name:'我的关注',
          component:myFollow
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/myNotice',
          name:'我的消息',
          component:myNotice
        }
      ]
    },{
      path: '/userpage',
      name: 'userpage',
      component: userpage,
      children: [
        {
          path: '/hostPage',
          name: '我的主页',
          component: hostPage
        }
      ]
    },{
      path:'/userpage',
      name:'userpage',
      component:userpage,
      children:[
        {
          path:'/modifyPass',
          name:'修改密码',
          component:modifyPass
        }
      ]
    },{
      path:'/problemDetail',
      name:'problemDetail',
      component:problemDetail
    },{
      path:'/writeAns',
      name:'writeAns',
      component:writeAns
    },{
      path:'/otherUserShow',
      name: 'otherUserShow',
      component: otherUserShow
    },{
      path: '/otherUserShow',
      name: 'otherUserShow',
      component: otherUserShow,
      children: [
        {
          path: '/otherUserAsk',
          name: 'otherUserAsk',
          component: otherUserAsk
        }
      ]
    },{
      path: '/otherUserShow',
      name: 'otherUserShow',
      component: otherUserShow,
      children: [
        {
          path: '/otherUserStore',
          name: 'otherUserStore',
          component: otherUserStore
        }
      ]
    },{
      path: '/manage',
      name: 'manage',
      component: manage,
      children: [
        {
          path: '/manage/managePolicy',
          name: 'managePolicy',
          component: managePolicy
        },
        {
          path: '/manage/manageUser',
          name: 'manageUser',
          component: manageUser
        },
        {
          path: 'manage/manageWrite',
          name: 'manageWrite',
          component: manageWrite
        }
      ]
    },{
      path:'/search',
      name:'search',
      component:search
    },{
      path:'/preLoad',
      name:'preLoad',
      component:preLoad
    },{
      path:'/retrievePassword',
      name:'retrievePassword',
      component:retrievePassword
    },{
      path:'/password',
      name:'password',
      component:password
    },{
      path:'/404',
      name:'404',
      component:notFound
    },{
      path:'*',
      redirect:'/404'
    }
  ]
})
