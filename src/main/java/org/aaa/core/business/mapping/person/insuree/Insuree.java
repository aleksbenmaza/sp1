package org.aaa.core.business.mapping.person.insuree;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.aaa.core.business.mapping.Vehicle;
import org.aaa.core.business.mapping.person.Person;

@Entity
@Table(name = "insurees")
public abstract class Insuree extends Person implements Serializable {

	public static final long serialVersionUID = 1168426245888642040L;

	@OneToMany(mappedBy = "insuree", cascade  = CascadeType.ALL)
	protected Set<Vehicle> vehicles;

	@Column(name = "bonus_malus")
	protected Float bonusMalus;

	public Insuree(){
		vehicles = new HashSet<Vehicle>();
	}

	public Float getBonusMalus() {
		return bonusMalus;
	}

	public void setBonusMalus(Float bonusMalus) {
		this.bonusMalus = bonusMalus;
	}

	public boolean addVehicle(Vehicle vehicle) {
		boolean bool;
		if(bool = vehicles.add(requireNonNull(vehicle)))
			vehicle.setInsuree(this);
		return bool;
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<Vehicle>(vehicles);
	}


}
