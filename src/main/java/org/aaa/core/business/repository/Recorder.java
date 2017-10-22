package org.aaa.core.business.repository;

import org.aaa.orm.entity.UpdatableEntity;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Recorder {

    void save(UpdatableEntity entity);

}
