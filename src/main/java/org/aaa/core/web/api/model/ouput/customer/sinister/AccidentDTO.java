package org.aaa.core.web.api.model.ouput.customer.sinister;

import org.aaa.core.business.mapping.sinister.accident.Accident;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class AccidentDTO extends SinisterDTO {

    @SerializedName("responsibility_rate")
    private float responsibilityRate;


    public AccidentDTO(Accident sinister) {
        super(sinister);
        responsibilityRate = sinister.getResponsibilityRate();
    }
}
