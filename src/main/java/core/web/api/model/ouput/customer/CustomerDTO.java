package core.web.model.domaintransfer.customer;

import static util.CommonUtils.replaceIfNull;

import core.business.model.mapping.UserAccount;
import core.business.model.mapping.person.insuree.Customer;
import core.web.model.domaintransfer.Presentation;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class CustomerDTO extends Presentation<Customer> {

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
        lastName     = customer.getLastName();
        firstName    = customer.getFirstName();
        address      = customer.getAddress();
        city         = customer.getCity();
        zipCode      = customer.getZipCode();
        phoneNumber  = customer.getPhoneNumber();
        bonusMalus   = customer.getBonusMalus();
        emailAddress = userAccount != null ? userAccount.getEmail() : null;
    }
}
