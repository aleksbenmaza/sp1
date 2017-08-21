package org.aaa.core.business.mapping.person;



import org.aaa.core.business.mapping.UserAccount;
import org.aaa.core.business.mapping.User;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public interface RegisteredUser extends User {

	void setUserAccount(UserAccount userAccount);
	
	UserAccount getUserAccount();

	static <T extends RegisteredUser> T requireNonNull(T toBeChecked) {
		return Objects.requireNonNull(toBeChecked);
	}
}
