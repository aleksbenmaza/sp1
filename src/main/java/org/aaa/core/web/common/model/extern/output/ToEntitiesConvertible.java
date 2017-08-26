package org.aaa.core.web.common.model.extern.output;

import org.aaa.core.business.mapping.Entity;

/**
 * Created by alexandremasanes on 26/08/2017.
 */
public interface ToEntitiesConvertible<T extends Entity> {

    T[] toEntities();
}
