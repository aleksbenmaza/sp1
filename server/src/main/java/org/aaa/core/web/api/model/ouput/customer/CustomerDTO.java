package org.aaa.core.web.api.model.ouput.customer;

import static org.aaa.util.ObjectUtils.ifNull;

import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class CustomerDTO extends DTO<Customer> {

    private static final long serialVersionUID = -5558499321500740901L;

    private long id;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("first_name")
    private String firstName;

    private String address;

    private String city;

    @SerializedName("zip_code")
    private int zipCode;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("email_address")
    private String emailAddress;

    @SerializedName("bonus_malus")
    private float bonusMalus;


    public CustomerDTO(Customer customer) {
        super(customer);
        UserAccount userAccount;

        userAccount  = customer.getUserAccount();

        id           = customer.getId();
        lastName     = customer.getNames().getLastName();
        firstName    = customer.getNames().getFirstName();
        address      = customer.getAddress().getNumber()
                     + (customer.getAddress().isBis() ? " bis" : "")
                     + customer.getAddress().getStreet();
        city         = customer.getAddress().getCity();
        zipCode      = customer.getAddress().getZipCode();
        phoneNumber  = customer.getPhoneNumber();
        bonusMalus   = customer.getBonusMalus();
        emailAddress = userAccount != null ? userAccount.getEmailAddress() : null;
    }
}
