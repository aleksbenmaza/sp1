<template>
  <navbar></navbar>
  <router-view></router-view>
</template>

<script>
  import Navbar from './Navbar.vue'
  import {store} from './store.js'
  import router from './router.js'

  export default {
    name: 'manager-panel',
    store,
    router,
    data () {
      return {
          manager : null
      }
    },
    components : {
        Navbar
    },
    mounted() {
        let self
        self = this
        this.$http.get(
            MANAGER_API_BASE_URI + '/managers', {
                params : {
                    token_id : this.$store.state.token.getId()
                }
            }
        ).then(
            response => self.manager = response.body
        )
    }
  }
</script>
