package org.aaa.core.web.api.model.ouput.admin;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.business.mapping.entity.Insurance;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandremasanes on 14/05/2017.
 */
public class InsuranceDTO extends org.aaa.core.web.api.model.ouput.publik.InsuranceDTO {

    private static final long serialVersionUID = -6727145142368695522L;

    @XmlAttribute(name = "min-deductible")
    @SerializedName("min_deductible")
    private double minDeductible;

    @XmlAttribute(name = "max-deductible")
    @SerializedName("max_deductible")
    private double maxDeductible;

    @XmlElement(name = "contracts-ids")
    @SerializedName("contracts_ids")
    private long[] contractsIds;

    @XmlElement(name = "rates-sinister-types-ids")
    @SerializedName("rates_sinister_types_ids")
    private Map<Long, Float> ratesSinisterTypesIds;

    public InsuranceDTO(Insurance insurance) {
        super(insurance);
        minDeductible    = insurance.getMinDeductible();
        maxDeductible    = insurance.getMaxDeductible();
        contractsIds     = getIds(insurance.getContracts());

        ratesSinisterTypesIds = new HashMap<>();

        insurance.getCoveragesBySinisterType().forEach(
                (k, v) -> ratesSinisterTypesIds.put(k.getId(), v.getRate())
        );
    }
}
