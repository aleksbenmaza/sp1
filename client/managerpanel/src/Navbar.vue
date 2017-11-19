<template xmlns:v-on="http://www.w3.org/1999/xhtml">
  <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
      <!-- Brand and toggle getByCustomer grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand">
          <img src="/resources/public/image/aaa.png" class="img-responsive" style="margin-top: -0.4rem;" width="50px" height="60px"/>
        </a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li class="">
            <a href="" accesskey="1" title="">
                        <span>Accueil</span>
            </a>
          </li>
          <li class="">
            <a href="nos-services" accesskey="2" title="">
                        <span>Services</span>
            </a>
          </li>
          <li class="">
            <a href="press" accesskey="4" title="">
                        <span>Press</span>
            </a>
          </li>
          <li class="">
            <a href="a-propos" accesskey="3" title="">
                        <span>À Propos</span>
            </a>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li role="presentation">
            <button v-on:click="signOut">
              Deconnexion
            </button>
          </li>
        </ul>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>
</template>

<script>
  export default {
      name : 'navbar',
      methods : {
          signOut() {
              this.$store.commit('setToken', null)
              this.$parent.$parent.$emit('tokenChanged')
          }
      }
  }
</script>
