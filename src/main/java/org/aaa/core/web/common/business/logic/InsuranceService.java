package org.aaa.core.web.common.business.logic;

import org.aaa.core.business.mapping.Insurance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService extends BaseService {

    public List<Insurance> getInsurances() {
        return dao.find(Insurance.class);
    }

    public Float getDeductibleValue(long insuranceId, float amount) {
        return dao.computeDeductibleValue(insuranceId, amount);
    }
}