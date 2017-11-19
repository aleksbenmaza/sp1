package org.aaa.core.web.common.model.foreign.output;

import org.aaa.core.business.mapping.entity.Make;

/**
 * Created by alexandremasanes on 12/11/2017.
 */
public interface MakeDTO extends ToEntityConvertible<Make> {

    String getName();

    ModelDTO[] getModelDTOs();
}
