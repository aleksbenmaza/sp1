package app.core.business.model.dao;

import app.core.business.model.mapping.Entity;
import app.core.business.model.mapping.IdentifiableByIdImpl;

import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Finder {

    <T extends Entity> List<T>  find(Class<T> entityClass);

    <T extends IdentifiableByIdImpl> List<T> find(Class<T> entityClass, long... ids);

    <T extends IdentifiableByIdImpl> T find(Class<T> entityClass, long id);

    <T extends IdentifiableByIdImpl> boolean exists(Class<T> entityClass, long id);

    <T extends IdentifiableByIdImpl> long getNextId(Class<T> entityClass);
}
