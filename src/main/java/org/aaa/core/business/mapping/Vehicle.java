package org.aaa.core.business.mapping;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.core.business.mapping.person.insuree.Insuree;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.business.mapping.sinister.accident.WithThirdPartyAccident;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.aaa.orm.entry.manytoone.Entry;
import org.hibernate.annotations.Where;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicles")
@SecondaryTable(name = "vehicles__insurees", pkJoinColumns = @PrimaryKeyJoinColumn(name = "vehicle_id", referencedColumnName = "id"))
public class Vehicle extends IdentifiableByIdImpl {

	public static final long serialVersionUID = 3403684733912100002L;

	@Column(name = "vin_number", unique = true)
	private String vinNumber;

	@Column(name = "value")
	private float value;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumns({
			@JoinColumn(
					name 	 = "model_id",
					nullable = false
			),
			@JoinColumn(
					name     = "year",
					nullable = false)
	})
	private ModelAndYear modelAndYear;

	@AssociationOverride(name = "key", joinColumns = @JoinColumn(columnDefinition = "id", referencedColumnName = "id"))
	private Entry<Insuree, Ownership> ownershipsInsuree;

	@OneToOne(mappedBy = "vehicle")
	@Where(clause = "active = 1")
	private Contract currentContract;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<WithThirdPartyAccident> withThirdPartyAccidents;

	public Vehicle(@NonNull ModelAndYear modelAndYear) {
		this.modelAndYear = modelAndYear;
		withThirdPartyAccidents = new HashSet<>();
		contracts  = new HashSet<>();
		modelAndYear.addVehicle(this);
	}

	public Vehicle(ModelAndYear modelAndYear, Entry<Insuree, Ownership> ownershipsInsuree) {
		this(modelAndYear);
		this.ownershipsInsuree = ownershipsInsuree;
		ownershipsInsuree.getKey().putOwnership(this, ownershipsInsuree.getValue());
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


	public ModelAndYear getModelAndYear() {
		return modelAndYear;
	}

	public Entry<Insuree, Ownership> getOwnershipsInsuree() {
		return ownershipsInsuree;
	}

	public void setOwnershipsInsuree(Entry<Insuree, Ownership> ownershipsInsuree) {
		this.ownershipsInsuree = ownershipsInsuree;
	}

	public Set<Sinister> getWithThirdPartyAccidents() {
		return new HashSet<Sinister>(withThirdPartyAccidents);
	}

	public boolean addThirdPartyAccident(WithThirdPartyAccident withThirdPartyAccident){
		return withThirdPartyAccidents.add(withThirdPartyAccident);
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
