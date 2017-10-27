package org.aaa.core.business.mapping.entity.person;



import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.User;

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
