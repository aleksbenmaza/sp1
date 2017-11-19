/**
 * Created by alexandremasanes on 13/11/2017.
 */

import Index from './Index.vue'

export default new VueRouter({
  mode : 'history',
  routes: [{
      path: '',
      name: 'index',
      component: Index
    }]
})
