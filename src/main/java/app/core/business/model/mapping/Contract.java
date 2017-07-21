package app.core.business.model.mapping;

import app.core.business.model.mapping.person.insuree.Customer;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contrats")
public class Contract extends IdentifiableByIdImpl implements ToBeChecked {

	public static final long serialVersionUID = 1683771937667628264L;

	@Enumerated
	@Column(name = "statut")
	private Status status;

	@Column(name = "montant")
	private float amount;

	@Column(name = "date_souscription")
	private Date subscriptionDate;

	@Column(name = "contrat")
	private String contractPath;

	@Column(name = "actif", nullable = false)
	private boolean active;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "client_id", referencedColumnName = "id")
	private Customer customer;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "vehicule_id", referencedColumnName = "id")
	private Vehicle vehicle;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "type_garantie_id", referencedColumnName = "id")
	private Insurance insurance;

	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
	private Set<Coverage> coverages;

	public Contract(Insurance insurance, Vehicle vehicle, Customer customer){
		requireNonNull(insurance, vehicle, customer);
		coverages      = new HashSet<Coverage>();
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

	public String getContractPath() {
		return contractPath;
	}

	public void setContractPath(String contractPath) {
		this.contractPath = contractPath;
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

	public Set<Coverage> getCoverages() {
		return new HashSet<Coverage>(coverages);
	}

	public boolean addCoverage(Coverage coverage) {
		return coverages.add(coverage);
	}

	Contract() {
	}
}
