package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.Ownership;
import org.aaa.core.business.mapping.Vehicle;
import org.aaa.core.web.api.model.ouput.DTO;

import com.google.gson.annotations.SerializedName;
import org.aaa.orm.entry.manytoone.Entry;

import java.util.Date;

/**
 * Created by alexandremasanes on 19/06/2017.
 */
public class VehicleDTO extends DTO<Vehicle> {

    private long id;

    @SerializedName("registration_number")
    private String registrationNumber;

    @SerializedName("purchase_date")
    private Date purchaseDate;

    public VehicleDTO(Vehicle vehicle) {
        super(vehicle);
        Entry<?, Ownership> ownership;
        id                   = vehicle.getId();
        if((ownership = vehicle.getOwnershipsInsuree()) != null) {
            registrationNumber = ownership.getValue().getRegistrationNumber();
            purchaseDate       = ownership.getValue().getPurchaseDate();
        }
    }
}
