package core.web.api.model.ouput.customer;

import core.business.model.mapping.Vehicle;
import core.web.api.model.ouput.Presentation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 19/06/2017.
 */
public class VehicleDTO extends Presentation<Vehicle> {

    private long id;

    @SerializedName("registration_number")
    private String registrationNumber;

    public VehicleDTO(Vehicle vehicle) {
        super(vehicle);
        id                   = vehicle.getId();
        registrationNumber   = vehicle.getRegistrationNumber();
    }
}
