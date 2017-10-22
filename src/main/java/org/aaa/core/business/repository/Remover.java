package org.aaa.core.business.repository;

import java.util.Map;
import java.util.Set;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.orm.entity.UpdatableEntity;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Remover {

    boolean remove(UpdatableEntity entity);

    <T extends UpdatableEntity> Map<T, Boolean> remove(Set<T> entities);

    <T extends IdentifiedByIdEntity> boolean remove(Class<T> entityClass, long... ids);

    <T extends IdentifiedByIdEntity> boolean remove(Class<T> entityClass, long id);
}
