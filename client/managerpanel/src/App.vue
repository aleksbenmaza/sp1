<template>
  <div id="app" class="container">
    <manager-panel v-if="authenticated"></manager-panel>
    <login-form v-else></login-form>
  </div>
</template>

<script>
  import setHeaderOption from './main.js'
  import {store, Token} from './store.js'
  import LoginForm  from './LoginForm.vue'
  import ManagerPanel from './ManagerPanel.vue'

  export default {
      name: 'app',
      store: store,
      data() {
          return {
              authenticated : Boolean(this.$store.state.token)
          }
      },
      components : {
          LoginForm,
          ManagerPanel
      },
      created() {
          let self
          self = this
          console.log('should be called')
          self.$store.dispatch('init')
          self.$on('tokenChanged', () => {
              console.log('called')
              self.authenticated = Boolean(self.$store.state.token)
          })
      }
  }
</script>

<style>

</style>
