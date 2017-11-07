package org.aaa.core.business.mapping.entity;

import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.sinister.Sinister;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.*;

@Entity
@Table(name = "contracts")
public class Contract extends IdentifiedByIdEntity implements ToBeChecked {

	private static final long serialVersionUID = 1683771937667628264L;

	@Enumerated
	@Column(
	) private Status status;

	@Column(
	) private float amount;

	@Column(
			name = "subscription_date"
	) private Date subscriptionDate;

	@Column(
			name = "has_contract_document"
	) private boolean contractDocPresent;

	@Column(
			nullable = false
	) private boolean active;

	@ManyToOne(
			cascade = CascadeType.ALL
	) @JoinColumn(
			name 				 = "customer_id",
			referencedColumnName = "id",
			nullable = false
	) private Customer customer;

	@ManyToOne(
			cascade = CascadeType.ALL
	) @JoinColumn(
			name = "vehicle_id",
			referencedColumnName = "id",
			nullable = false
	) private Vehicle vehicle;

	@ManyToOne(
			cascade = CascadeType.ALL
	) @JoinColumn(
			name 				 = "insurance_id",
			referencedColumnName = "id",
			nullable = false
	) private Insurance insurance;

	@OneToMany(
			cascade = CascadeType.ALL,
			mappedBy = "contract"
	)
	private Set<Sinister> sinisters;

	public Contract(
			Insurance insurance,
			Vehicle vehicle,
			Customer customer
	) {
		sinisters = new HashSet<>();
		requireNonNull(insurance, vehicle, customer);
		this.insurance = insurance;
		this.vehicle   = vehicle;
		this.customer  = customer;
		insurance.addContract(this);
		vehicle.addContract(this);
		customer.addContract(this);
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public boolean isContractDocPresent() {
		return contractDocPresent;
	}

	public void setContractDocPresent(boolean contractDocPresent) {
		this.contractDocPresent = contractDocPresent;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		if(active)
			vehicle.setCurrentContract(this);
		this.active = active;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public Set<Sinister> getSinisters() {
		return new HashSet<>(sinisters);
	}

	public boolean addSinister(Sinister sinister) {
		return sinisters.add(sinister);
	}

	Contract() {}
}
