package org.aaa.core.business.mapping.person;

import org.aaa.core.business.mapping.UserAccount;
import org.aaa.core.business.mapping.person.insuree.Customer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "managers")
public class Manager extends Person implements RegisteredUser {

	public static final long serialVersionUID = 6723623575574622116L;

	@OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
	private Set<Customer> customers;

	{
		customers = new HashSet<Customer>();
	}

	@Override
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public Set<Customer> getCustomers() {
		return new HashSet<>(customers);
	}

	public boolean addCustomer(Customer customer) {
		boolean added;
		added = customers.add(requireNonNull(customer));
		if(customer.getManager() != this)
			customer.setManager(this);
		return added;
	}

	public boolean removeCustomer(Customer customer) {
		if(customer.getManager().equals(this))
			customer.setManager(null);
		return customers.remove(customer);
	}
}