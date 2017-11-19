import Vue from 'vue'
import App from './App.vue'
import {auth_callback} from './store.js'

Vue.use(VueResource)
Vue.use(Vuex)
Vue.use(VueRouter)

Vue.config.devtools = true



export default function setRequestHeader(header_name, header_value) {
    Vue.http.headers[header_name] = header_value
}

new Vue({
    el: '#app',
    data() {
    },
    render: h => h(App),
    created() {
      Vue.http.interceptors.push((request, next) => {
        console.log('titi')
        request.headers.set('Authorization', auth_callback())
        next()
      })
    }
})
