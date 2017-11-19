package org.aaa.core.web.common.model.foreign.output;

import org.aaa.core.business.mapping.entity.Year;

/**
 * Created by alexandremasanes on 12/11/2017.
 */
public interface YearDTO extends ToEntityConvertible<Year> {

    short getValue();
}
