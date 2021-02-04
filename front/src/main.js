// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import store from './store'
// import NProgress from 'nprogress'
// import 'nprogress/nprogress.css'

Vue.prototype.$http = axios;
Vue.use(ElementUI)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})

// router.beforeEach((to, from, next) => {
//   NProgress.start()
//   NProgress.set(0.1)
//   next()
// })
// router.afterEach(() => {
//   setTimeout(() => NProgress.done(), 500)

