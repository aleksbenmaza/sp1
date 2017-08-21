package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.IdentifiableById.NULL_ID;

import org.aaa.core.business.mapping.Insurance;
import org.aaa.core.business.mapping.Make;
import org.aaa.core.business.mapping.Model;
import org.aaa.core.web.app.model.persistence.Session;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by alexandremasanes on 02/03/2017.
 */
@Service
public class ApiService extends BaseService {

    public List<String> getUserNotifications(Session Session) {
        return Session.flushNotifications();
    }

    public List<Insurance> getAllInsurances() {
        return dao.find(Insurance.class);
    }

    public List<Make> getMakesByName(String name) {
        return dao.searchMakes(name);
    }

    public List<Model> getModelsByNameAndMakeId(String name, long id) {
        return id == NULL_ID ? dao.searchModels(name) : dao.searchModels(name, id);
    }

    public Float getDeductibleValue(long insuranceId, float amount) {
        return dao.computeDeductibleValue(insuranceId, amount);
    }
}
