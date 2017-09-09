package org.aaa.core.web.api.model.ouput.admin;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.business.mapping.Insurance;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by alexandremasanes on 14/05/2017.
 */
public class InsuranceDTO extends org.aaa.core.web.api.model.ouput.publik.InsuranceDTO {

    @XmlAttribute(name = "min-deductible")
    @SerializedName("min_deductible")
    private double minDeductible;

    @XmlAttribute(name = "max-deductible")
    @SerializedName("max_deductible")
    private double maxDeductible;

    @XmlElement(name = "contracts-ids")
    @SerializedName("contracts_ids")
    private long[] contractsIds;

    @XmlElement(name = "sinister-types-ids")
    @SerializedName("sinister_types_ids")
    private long[] sinisterTypesIds;

    public InsuranceDTO(Insurance insurance) {
        super(insurance);
        minDeductible    = insurance.getMinDeductible();
        maxDeductible    = insurance.getMaxDeductible();
        contractsIds     = getIds(insurance.getContracts());
        //sinisterTypesIds = getIds(insurance.getCoveragesBySinisterType());


    }
}
