package org.aaa.core.business.mapping;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.business.mapping.person.Person;
import org.checkerframework.checker.nullness.qual.Nullable;


@Entity
@Table(name = "user_accounts")
public class UserAccount extends org.aaa.core.business.mapping.Entity implements Serializable {

	public static final long serialVersionUID = 1482252304755392540L;

	public static final class Id implements Serializable {

		@OneToOne(targetEntity = Person.class, cascade = CascadeType.ALL)
		@JoinColumn(name = "id", referencedColumnName = "id")
		private RegisteredUser user;

		public <T extends Person & RegisteredUser> Id(T user) {
			this.user = user;
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

		Id() {}
	}

	@EmbeddedId
	private Id id;

	@Column(name = "email_address")
	private String emailAddress;

	@Column
	private String hash;

	@OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Token token;

	public <T extends Person & RegisteredUser> UserAccount(T user) {
		check(requireNonNull(user).getUserAccount() == null);
		user.setUserAccount(this);
		this.id = new Id(user);
	}

	public <T extends Person & RegisteredUser> UserAccount(
			T 	   user,
			Token token
	) {
		this(user);
		check(requireNonNull(token).getUserAccount() == null);
		setToken(token);
	}

	public RegisteredUser getUser(){
		return id.user;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(@Nullable Token token) {
		this.token = token;
		if(token != null && token.getUserAccount() != this)
			token.setUserAccount(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserAccount that = (UserAccount) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	UserAccount() {
	}
}