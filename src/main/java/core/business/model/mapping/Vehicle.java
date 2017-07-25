package core.business.model.mapping;

import javax.persistence.*;
import javax.persistence.Entity;

import core.business.model.mapping.person.insuree.Insuree;
import core.business.model.mapping.sinister.Sinister;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicules")
public class Vehicle extends IdentifiableByIdImpl {

	public static final long serialVersionUID = 3403684733912100002L;

	@Column(name = "vin", unique = true)
	private String vinNumber;

	@Column(name = "immatriculation", unique = true)
	private String registrationNumber;

	@Column(name = "valeur")
	private float value;

	@Column(name = "date_achat")
	private Date purchaseDate;

	@Column(name = "carte_grise")
	private String registrationFilePath;

	@Fetch(FetchMode.JOIN)
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "modele_id", referencedColumnName = "id")
	private Model model;

	@Fetch(FetchMode.JOIN)
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "assure_id", referencedColumnName = "id")
	private Insuree insuree;

	@Fetch(FetchMode.JOIN)
	@OneToOne(mappedBy = "vehicle", fetch = FetchType.EAGER)
	@Where(clause = "active = 1")
	private Contract currentContract;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	@OneToMany(targetEntity = Sinister.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Sinister> sinisters;

	public Vehicle() {
	}

	public Vehicle(Model model) {
		this.model = requireNonNull(model);
		sinisters  = new HashSet<Sinister>();
		contracts  = new HashSet<Contract>();
		model.addVehicle(this);
	}

	public Vehicle(Model model, Insuree insuree) {
		this(model);
		this.insuree = requireNonNull(insuree);

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

	public String getRegistrationFilePath() {
		return registrationFilePath;
	}

	public void setRegistrationFilePath(String registrationFilePath) {
		this.registrationFilePath = registrationFilePath;
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

	public Set<Sinister> getSinisters() {
		return new HashSet<Sinister>(sinisters);
	}

	public boolean addSinister(Sinister sinister){
		return sinisters.add(sinister);
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
}
