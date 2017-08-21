package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.Vehicle;
import org.aaa.core.web.api.model.ouput.DTO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 19/06/2017.
 */
public class VehicleDTO extends DTO<Vehicle> {

    private long id;

    @SerializedName("registration_number")
    private String registrationNumber;

    public VehicleDTO(Vehicle vehicle) {
        super(vehicle);
        id                   = vehicle.getId();
        registrationNumber   = vehicle.getRegistrationNumber();
    }
}
