package app.core.business.model.mapping.person;



import app.core.business.model.mapping.UserAccount;
import app.core.web.model.persistence.User;

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
