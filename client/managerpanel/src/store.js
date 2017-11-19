/**
 * Created by alexandremasanes on 12/11/2017.
 */

export class Token {

  constructor(value, id) {
    this.value = value
    this.id    = id
  }

  getValue() {
    return this.value
  }

  setValue(value) {
    this.value = value
  }

  getId() {
    return this.id
  }

  valid() {
    return Boolean(this.value && this.id)
  }
}

export const auth_callback = () => store.state.token ?
  store.state.token.getValue() :
  null

export const store = new Vuex.Store({
  state : {
      token : null
  },
  getters : {
      getToken() {
          return token
      }
  },
  mutations : {
      setToken(state, token) {
          state.token = token
          localStorage.setItem(Token.name, token)
      }
  },
  actions : {
    init(context) {
      this.state.token = localStorage.getItem(Token.name)
    },
    destroy(context) {

    }
  }
})
