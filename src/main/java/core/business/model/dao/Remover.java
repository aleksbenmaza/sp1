package app.core.business.model.dao;

import java.util.Map;
import java.util.Set;

import app.core.business.model.mapping.Entity;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Remover {

    boolean remove(Entity entity);

    <T extends Entity> Map<T, Boolean> remove(Set<T> entities);
}
