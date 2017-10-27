package org.aaa.core.web.api.model.ouput.publik;

import org.aaa.core.business.mapping.entity.Insurance;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
@XmlRootElement
public class InsuranceDTO extends DTO<Insurance> {

    private long id;

    private String code;

    private String name;

    @XmlAttribute(name = "default-amount")
    @SerializedName("default_amount")
    private float defaultAmount;

    public InsuranceDTO(Insurance insurance) {
        super(insurance);
        id            = insurance.getId();
        code          = insurance.getCode();
        name          = insurance.getName();
        defaultAmount = insurance.getDefaultAmount();
    }
}
