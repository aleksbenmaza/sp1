package org.aaa.core.web.common.business.logic;

import org.aaa.core.business.mapping.entity.Insurance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsuranceService extends BaseService {

    @Transactional(readOnly = true)
    public List<Insurance> getInsurances(long... ids) {
        return dao.find(Insurance.class, ids);
    }

    public Float getDeductibleValue(long insuranceId, float amount) {
        return dao.computeDeductibleValue(insuranceId, amount);
    }
}