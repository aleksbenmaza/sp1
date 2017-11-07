package org.aaa.core.business.mapping.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import org.aaa.core.business.mapping.entity.Vehicle;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 27/10/2017.
 */

@EntityView(Vehicle.class)
public interface VehicleCountByInsureeAndModel extends Serializable {

    @Mapping("CONCAT(ownership.key.firstName, ' ', insuree.key.lastName)")
    String getInsureeFullname();

    @Mapping("CONCAT(model.make.name, ' ', model.name, ' ', year.value")
    String getMakeModelYearName();

    @Mapping("COUNT(*)")
    long getCount();
}
