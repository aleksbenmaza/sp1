package org.aaa.core.web.api.model.ouput.admin;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.business.mapping.entity.person.Manager;
import org.aaa.core.web.api.model.ouput.DTO;

/**
 * Created by alexandremasanes on 13/11/2017.
 */
public class ManagerDTO extends DTO<Manager> {

    private static final long serialVersionUID = -2143617144920206998L;

    private long   id;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("customers_ids")
    private long[] customersIds;

    public ManagerDTO(Manager entity) {
        super(entity);
        id           = entity.getId();
        lastName     = entity.getNames().getLastName();
        firstName    = entity.getNames().getFirstName();
        customersIds = getIds(entity.getCustomers());
    }
}
