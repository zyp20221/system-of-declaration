import Vue from 'vue'
import App from './App.vue'
import router from './router'
//引入elementui组件
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import setupAxiosInterceptors from './utils/axios-config';
setupAxiosInterceptors(); // Setup Axios interceptors
Vue.config.productionTip = false
Vue.use(ElementUI);
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')/*指定挂载的元素*/
