package org.aaa.core.business.mapping.entity.person;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.business.mapping.entity.UserAccount;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person extends IdentifiedByIdEntity {

	private static final long serialVersionUID = 5018887767387110200L;

	@Column(
			name = "first_name"
	) private String firstName;

	@Column(
			name = "last_name"
	) private String lastName;

	@Column(
	) private String address;

	@Column(
	) private String city;

	@Column(
			name = "zip_code"
	) private int zipCode;

	@Column(
			name = "phone_number"
	) private String phoneNumber;


	@OneToOne(
			mappedBy = "id.user",
			cascade  = CascadeType.ALL,
			fetch    = FetchType.LAZY
	)
	protected UserAccount userAccount;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}