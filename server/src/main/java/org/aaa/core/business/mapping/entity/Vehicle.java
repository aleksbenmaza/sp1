package org.aaa.core.business.mapping.entity;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.core.business.mapping.embeddable.Ownership;
import org.aaa.core.business.mapping.entity.person.insuree.Insuree;
import org.aaa.core.business.mapping.entity.sinister.Sinister;
import org.aaa.core.business.mapping.entity.sinister.accident.WithThirdPartyAccident;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.orm.entry.manytoone.Entry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hibernate.annotations.Where;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicles")
@SecondaryTable(name = "vehicles__insurees", pkJoinColumns = @PrimaryKeyJoinColumn(name = "vehicle_id", referencedColumnName = "id"))
public class Vehicle extends IdentifiedByIdEntity {

	private static final long serialVersionUID = 3403684733912100002L;

	@Column(name = "vin_number", unique = true)
	private String vinNumber;

	@Column(name = "value")
	private float value;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "model_id", referencedColumnName = "id")
	private Model model;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "year_id", referencedColumnName = "id")
	private Year year;

	@Embedded
	@AssociationOverride(
			name 		= "key",
			joinColumns = @JoinColumn(
					table 				 = "vehicles__insurees",
					name  				 = "insuree_id",
					referencedColumnName = "id"
			)
	)
	private Entry<Insuree, Ownership> ownershipsInsuree;

	@OneToOne(mappedBy = "vehicle")
	@Where(clause = "active = 1")
	private Contract currentContract;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<WithThirdPartyAccident> withThirdPartyAccidents;

	public Vehicle(String vinNumber, @NonNull Model model, @NonNull Year year) {
		this.vinNumber = vinNumber;
		this.model = model;
		this.year  = year;
		withThirdPartyAccidents = new HashSet<>();
		contracts  = new HashSet<>();
		model.addVehicle(this);
		year.addVehicle(this);
	}

	public Vehicle(String vinNumber, Model model, Year year, Entry<Insuree, Ownership> ownershipsInsuree) {
		this(vinNumber, model, year);
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

	public Model getModel() {
		return model;
	}

	public Year getYear() {
		return year;
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

	@Override
	public final int hashCode() {
		return vinNumber.hashCode();
	}

	Vehicle() {}
}
