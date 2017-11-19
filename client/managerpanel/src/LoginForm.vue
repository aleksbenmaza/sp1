<template xmlns:v-on="http://www.w3.org/1999/xhtml">
  <form id="login_form" class="form-horizontal text-center">
    <div class="form-group">
      <label>Email</label>
      <br/>
      <input type="email" v-model="login.email_address" id="email" size="30"/>
    </div>
    <div class="form-group">
      <label>Mot de passe</label>
      <br/>
      <input type="password" v-model="login.password" id="pwd" size="30"/>
    </div>
    <div class="form-group">
      <button type="button" id="log_button" class="btn btn-default" v-on:click="signIn">Connexion</button>
    </div>
  </form>
</template>

<script>
  import {store, Token} from './store.js'

  export default {
    name: 'login-form',
    store : store,
    data () {
      return {
        login : {}
      }
    },
    methods : {
        signIn() {
          let self = this
          this.$http.post(
              MANAGER_API_BASE_URI + '/tokens',
              this.login
          ).then(response => {
              console.log(response.headers.get('Authorization'), response.headers.get('Location'))
              self.$store.commit('setToken',
                  new Token(
                      response.headers.get('Authorization') || null,
                      parseInt(response.headers.getAll('Location') || null)
                  )
              )
              self.$parent.$emit('tokenChanged');
            }
          )
        }
    }
  }
</script>

<style>

</style>
