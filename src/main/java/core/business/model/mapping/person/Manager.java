package core.business.model.mapping.person;

import core.business.model.mapping.UserAccount;
import core.business.model.mapping.person.insuree.Customer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="administrateurs")
public class Manager extends Person implements RegisteredUser {

	public static final long serialVersionUID = 6723623575574622116L;

	@OneToMany(mappedBy = "admin",
			   cascade  = {CascadeType.PERSIST, CascadeType.REMOVE})
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
		if(customer.getAdmin() != this)
			customer.setAdmin(this);
		return added;
	}

	public boolean removeCustomer(Customer customer) {
		if(customer.getAdmin().equals(this))
			customer.setAdmin(null);
		return customers.remove(customer);
	}
}
