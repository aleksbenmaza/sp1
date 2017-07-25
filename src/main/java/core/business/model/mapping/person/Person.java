package core.business.model.mapping.person;

import javax.persistence.*;
import javax.persistence.Entity;

import core.business.model.mapping.IdentifiableByIdImpl;
import core.business.model.mapping.UserAccount;

import java.io.Serializable;

@Entity
@Table(name = "personnes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class      Person
		        extends    IdentifiableByIdImpl
		        implements Serializable {

	public static final long serialVersionUID = 5018887767387110200L;

	@Column(name = "prenom")
	private String firstName;

	@Column(name = "nom")
	private String lastName;

	@Column(name = "adresse")
	private String address;

	@Column(name = "ville")
	private String city;

	@Column(name = "code_postal")
	private int zipCode;

	@Column(name = "tel")
	private String phoneNumber;

	@OneToOne(mappedBy = "id.user",
			  cascade  = {CascadeType.PERSIST, CascadeType.REMOVE})
	protected UserAccount userAccount; //EAGER + transient when mapped by an interface
	
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