package org.aaa.core.business.repository;

import org.aaa.orm.entity.BaseEntity;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Finder {

    <T extends BaseEntity>                    List<T> find(Class<T> entityClass);

    <T extends IdentifiedByIdEntity> List<T> find(Class<T> entityClass, long... ids);

    <T extends IdentifiedByIdEntity> T       find(Class<T> entityClass, long id);

    <T extends IdentifiedByIdEntity> boolean has(Class<T> entityClass, long id);

    <T extends IdentifiedByIdEntity> long    getNextId(Class<T> entityClass);
}
