package org.aaa.core.business.mapping.entity.person.insuree;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;

import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.ToBeChecked;
import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.business.mapping.entity.person.RegisteredUser;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

@Entity
@Table(name = "customers")
public class Customer extends Insuree implements RegisteredUser, ToBeChecked {

	private static final long serialVersionUID = -4842054876473218101L;

	@Column(name = "has_sepa_document")
	private boolean sepaDocumentPresent;

	@Enumerated
	@Column(

	) private Status status;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "manager_id", referencedColumnName = "id")
	private Manager manager;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Contract> contracts;

	public Customer() {
		contracts = new HashSet<>();
	}

	public Customer(Manager manager) {
		this();
		setManager(IdentifiedByIdEntity.requireNonNull(manager));
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
		return contracts;
	}

	public boolean addContract(Contract contract) {
		return contracts.add(IdentifiedByIdEntity.requireNonNull(contract));
	}
}