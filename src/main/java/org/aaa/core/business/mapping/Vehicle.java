package org.aaa.core.business.mapping;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.core.business.mapping.person.insuree.Insuree;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.business.mapping.sinister.accident.ThirdPartyAccident;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.hibernate.annotations.Where;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle extends IdentifiableByIdImpl {

	public static final long serialVersionUID = 3403684733912100002L;

	@Column(name = "vin_number", unique = true)
	private String vinNumber;

	@Column(name = "registration_number", unique = true)
	private String registrationNumber;

	@Column(name = "value")
	private float value;

	@Column(name = "purchase_date")
	private Date purchaseDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false)
	private Model model;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "insuree_id", referencedColumnName = "id")
	private Insuree insuree;

	@OneToOne(mappedBy = "vehicle")
	@Where(clause = "active = 1")
	private Contract currentContract;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<ThirdPartyAccident> thirdPartyAccidents;

	public Vehicle(@NonNull Model model) {
		this.model = requireNonNull(model);
		thirdPartyAccidents = new HashSet<ThirdPartyAccident>();
		contracts  = new HashSet<Contract>();
		model.addVehicle(this);
	}

	public Vehicle(Model model, Insuree insuree) {
		this(model);
		this.insuree = requireNonNull(insuree);
		insuree.addVehicle(this);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Model getModel() {
		return model;
	}

	public Insuree getInsuree() {
		return insuree;
	}

	public void setInsuree(Insuree insuree) {
		insuree.addVehicle(this);
		this.insuree = insuree;
	}

	public Set<Sinister> getThirdPartyAccidents() {
		return new HashSet<Sinister>(thirdPartyAccidents);
	}

	public boolean addThirdPartyAccident(ThirdPartyAccident thirdPartyAccident){
		return thirdPartyAccidents.add(thirdPartyAccident);
	}

	public boolean addContract(Contract contract) {
		return contracts.add(contract);
	}

	public Set<Contract> getContracts() {
		return contracts;
	}

	public Contract getCurrentContract() {
		return currentContract;
	}

	public void setCurrentContract(Contract currentContract) {
		if(this.currentContract != null && !this.currentContract.equals(currentContract))
			this.currentContract.setActive(false);
		this.currentContract = currentContract;
	}

	Vehicle() {}
}
