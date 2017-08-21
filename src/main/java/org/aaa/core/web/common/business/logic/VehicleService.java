package org.aaa.core.web.common.business.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by alexandremasanes on 04/04/2017.
 */
@Service
public class VehicleService extends BaseService {

    @Value("vehicleService.documentsDir.idCard")
    private String registrationDocumentDir;

    public String getRegistrationDocumentDir() {
        return registrationDocumentDir;
    }
}
