package org.aaa.core.business.mapping.entity.person.insuree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.aaa.core.business.mapping.entity.Ownership;
import org.aaa.core.business.mapping.entity.Vehicle;
import org.aaa.core.business.mapping.entity.person.Person;

@Entity
@Table(name = "insurees")
public abstract class Insuree extends Person implements Serializable {

	private static final long serialVersionUID = 1168426245888642040L;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
			name = "vehicles__insurees",
			joinColumns = @JoinColumn(
					name = "insuree_id",
					referencedColumnName = "id"
			)
	)
	@MapKeyJoinColumn(name = "vehicle_id", referencedColumnName = "id")
	protected Map<Vehicle, Ownership> ownershipsByVehicle;

	@Column(name = "bonus_malus")
	protected Float bonusMalus;

	public Insuree(){
		ownershipsByVehicle = new HashMap<>();
	}

	public Float getBonusMalus() {
		return bonusMalus;
	}

	public void setBonusMalus(Float bonusMalus) {
		this.bonusMalus = bonusMalus;
	}

	public Ownership putOwnership(Vehicle vehicle, Ownership ownership) {
		return ownershipsByVehicle.put(vehicle, ownership);
	}

	public Map<Vehicle, Ownership> getOwnershipsByVehicle() {
		return new HashMap<>(ownershipsByVehicle);
	}
}
