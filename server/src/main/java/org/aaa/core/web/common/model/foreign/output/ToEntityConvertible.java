package org.aaa.core.web.common.model.foreign.output;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

/**
 * Created by alexandremasanes on 26/08/2017.
 */
public interface ToEntityConvertible<T extends IdentifiedByIdEntity> {

    T toEntity();
}
