package org.aaa.core.web.app.util;

import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.business.mapping.entity.person.Person;
import org.aaa.core.business.mapping.entity.person.RegisteredUser;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.person.User;

/**
 * Created by alexandremasanes on 17/08/2017.
 */
public final class TagUtils {

    //Checks to see if Object 'o' is an instance of the class in the string "className"
    public static boolean isRegistered(User user) {
        return user instanceof RegisteredUser;
    }

    public static boolean isCustomer(User user) {
        return user instanceof Customer;
    }

    public static boolean isManager(User user) {
        return user instanceof Manager;
    }

    public static String getShortenedFullName(Person person) {
        String fullName;

        fullName = person.getFirstName() + " " + person.getLastName() + "   ";
        fullName = fullName.length() > 20 ? fullName.substring(0, 15) + "..." : fullName;

        return fullName;
    }

    TagUtils() {}
}
