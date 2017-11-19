package org.aaa.core.business.mapping.entity.person;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.aaa.orm.custom.Address;
import org.aaa.orm.custom.Names;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.business.mapping.entity.UserAccount;
import org.hibernate.annotations.*;

@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = "nir_number"))
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person extends IdentifiedByIdEntity {

	private static final long serialVersionUID = 5018887767387110200L;

	@Column(name = "nir_number")
	private String nirNumber;


	@Type(type = "namesType")
	@Columns(columns = {
			@Column(name = "first_name"),
			@Column(name = "last_name")
	})
	private Names names;

	private Address address;

	@Column(
			name = "phone_number"
	) private String phoneNumber;


	@OneToOne(
			mappedBy = "id.user",
			cascade  = CascadeType.ALL,
			fetch    = FetchType.LAZY
	)
	protected UserAccount userAccount;

	public Person(String nirNumber) {
		this.nirNumber = nirNumber;
	}

	public String getNirNumber() {
		return nirNumber;
	}

	public Names getNames() {
		return names;
	}

	public void setNames(Names names) {
		this.names = names;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public final int hashCode() {
		return nirNumber.hashCode();
	}

	protected Person() {}
}