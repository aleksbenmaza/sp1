package org.aaa.core.web.api.model.ouput;

import org.aaa.core.business.mapping.Entity;
import org.aaa.core.business.mapping.IdentifiableById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public abstract class DTO<E extends Entity> implements Serializable {

    public DTO(E entity) {
        if(entity.updatedAt() == null)
            throw new RuntimeException("Entity must be persisted before !");
    }

    public static <E extends Entity, P extends DTO<E>> List<P> fromCollection(
            Collection<E> entities, Function<E, P> function) {
        List<P> result;

        result = new ArrayList<>();


        for(E entity : entities)
            result.add(function.apply(entity));

        return result;
    }

    protected static long[] getIds(Collection<? extends IdentifiableById> collection) {
        long[] ids;
        int i;

        ids = new long[collection.size()];
        i   = 0;

        for(IdentifiableById element : collection)
            ids[i++] = element.getId();

        return ids;
    }
}
