package org.aaa.core.business.mapping.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.aaa.core.business.mapping.entity.person.RegisteredUser;
import org.aaa.core.business.mapping.entity.person.Person;

import org.aaa.orm.entity.UpdatableEntity;
import org.checkerframework.checker.nullness.qual.Nullable;


@Entity
@Table(name = "user_accounts")
public class UserAccount extends UpdatableEntity {

	private static final long serialVersionUID = 1482252304755392540L;

	@Embeddable
	public static class Id implements Serializable {

		private static final long serialVersionUID = -6242302251593705273L;

		@OneToOne(targetEntity = Person.class, cascade = CascadeType.ALL)
		@JoinColumn(name = "id", referencedColumnName = "id")
		private RegisteredUser user;

		public <U extends  Person & RegisteredUser> Id(U user) {
			check(requireNonNull(user).getUserAccount() == null);
			this.user = user;
		}

		public RegisteredUser getUser() {
			return user;
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof Id && o.hashCode() == this.hashCode();
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

	@OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Token token;

	public UserAccount(Id id) {
		this.id = id;
		id.user.setUserAccount(this);
	}

	public UserAccount(
			Id id,
			Token token
	) {
		this(id);
		check(requireNonNull(token).getUserAccount() == null);
		setToken(token);
	}

	public final Id getId() {
		return id;
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
		return o instanceof UserAccount && this.id.equals(((UserAccount) o).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	UserAccount() {}
}