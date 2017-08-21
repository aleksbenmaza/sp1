package org.aaa.core.business.mapping.person.insuree;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.core.business.mapping.person.Manager;
import org.aaa.core.business.mapping.person.RegisteredUser;
import org.aaa.core.business.mapping.ToBeChecked;
import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.UserAccount;

@Entity
@Table(name = "customers")
public class Customer extends Insuree implements RegisteredUser, ToBeChecked {

	public static final long serialVersionUID = -4842054876473218101L;

	@Column(name = "has_sepa_document")
	private boolean sepaDocumentPresent;

	@Enumerated
	@Column(

	) private Status status;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "manager_id", referencedColumnName = "id")
	private Manager manager;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	public Customer() {
		contracts = new HashSet<Contract>();
	}

	public Customer(Manager manager) {
		this();
		setManager(org.aaa.core.business.mapping.Entity.requireNonNull(manager));
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		if(this.manager != null)
			this.manager.removeCustomer(this);
		this.manager = manager;
		if(manager != null)
			manager.addCustomer(this);
	}

	@Override
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public boolean isSepaDocumentPresent() {
		return sepaDocumentPresent;
	}

	public void setSepaDocumentPresent(boolean sepaDocumentPresent) {
		this.sepaDocumentPresent = sepaDocumentPresent;
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	public Set<Contract> getContracts() {
		return new HashSet<Contract>(contracts);
	}

	public boolean addContract(Contract contract) {
		return contracts.add(org.aaa.core.business.mapping.Entity.requireNonNull(contract));
	}
}