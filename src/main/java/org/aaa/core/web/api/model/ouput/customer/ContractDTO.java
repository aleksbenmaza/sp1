package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.ToBeChecked;

import org.aaa.core.web.api.model.ouput.DTO;
import org.aaa.core.web.api.model.ouput.publik.InsuranceDTO;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class ContractDTO extends DTO<Contract> {

    private long id;

    private ToBeChecked.Status status;

    private float amount;

    private boolean active;

    @SerializedName("subscription_date")
    private Date subscriptionDate;

    @SerializedName("insurance")
    private InsuranceDTO insuranceDTO;

    @SerializedName("vehicle")
    private VehicleDTO vehicleDTO;

    public ContractDTO(Contract contract) {
        super(contract);
        id               = contract.getId();
        status           = contract.getStatus();
        amount           = contract.getAmount();
        active           = contract.isActive();
        subscriptionDate = contract.getSubscriptionDate();
        insuranceDTO     = new InsuranceDTO(contract.getInsurance());
        vehicleDTO       = new VehicleDTO(contract.getVehicle());
    }
}
