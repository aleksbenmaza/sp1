package org.aaa.core.web.common.business.logic.vehicle;

import java.net.URISyntaxException;

/**
 * Created by alexandremasanes on 08/11/2017.
 */

//@Component
public class VehicleTasks {

    private VehicleService vehicleService;

    //@Scheduled(cron = "0 0 * * * *")
    public void importMakes() throws URISyntaxException {
        vehicleService.importNewMakes();
    }
}
