package org.aaa.core.web.common.model.foreign.output;

import org.aaa.core.business.mapping.entity.Model;

/**
 * Created by alexandremasanes on 12/11/2017.
 */
public interface ModelDTO extends ToEntityConvertible<Model> {

    String getName();

    YearDTO[] getYearDTOs();
}
