package app.core.business.model.mapping;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import app.core.business.model.mapping.person.Person;
import app.core.business.model.mapping.person.RegisteredUser;


@Entity
@Table(name = "comptes_utilisateur")
public class UserAccount extends app.core.business.model.mapping.Entity implements Serializable {

	public static final long serialVersionUID = 1482252304755392540L;

	@Embeddable
	public static class Id implements Serializable {

		@OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
		@JoinColumn(name = "id", referencedColumnName = "id")
		private RegisteredUser user;

		public <T extends Person & RegisteredUser> Id(T user) {
			this.user = requireNonNull(user);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;

			return user.equals(id.user);
		}

		@Override
		public int hashCode() {
			return user.hashCode();
		}

		Id() {

		}
	}

	@EmbeddedId
	private Id id;

	@Column
	private String email;
	
	@Column
	private String hash;
	
	@Column(name = "api_key", updatable = false)
	private String apiKey;

	@OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
	private Token token;

	public <T extends Person & RegisteredUser> UserAccount(T user) {
		if(user.getUserAccount() != null)
			throw new BusinessException();
		user.setUserAccount(this);
		this.id = new Id(user);
	}

	public RegisteredUser getUser(){
		return id.user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
		if(token != null && token.getUserAccount() != this)
			token.setUserAccount(this);
	}

	UserAccount() {
	}
}