package org.aaa.core.web.api.model.ouput.customer.sinister;

import org.aaa.core.business.mapping.entity.sinister.accident.Accident;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class AccidentDTO extends SinisterDTO {

    private static final long serialVersionUID = -780487746637395833L;

    @SerializedName("responsibility_rate")
    private float responsibilityRate;


    public AccidentDTO(Accident sinister) {
        super(sinister);
        responsibilityRate = sinister.getResponsibilityRate();
    }
}
