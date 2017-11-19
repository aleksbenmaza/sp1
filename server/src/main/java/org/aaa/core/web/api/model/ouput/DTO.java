package org.aaa.core.web.api.model.ouput;


import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.orm.entity.identifiable.IdentifiableById;

import static java.lang.reflect.Array.newInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public abstract class DTO<E extends IdentifiedByIdEntity> implements Serializable {

    private static final long serialVersionUID = -4619619584552930568L;

    public DTO(E entity) {
        if(entity.getUpdateTime() == null)
            throw new RuntimeException("IdentifiedByIdEntity must be persisted before !");
    }

    public static <E extends IdentifiedByIdEntity, P extends DTO<E>> List<P> fromCollection(
            Collection<E> entities, Function<E, P> function) {
        List<P> result;

        result = new ArrayList<>();

        for(E entity : entities)
            result.add(function.apply(entity));

        return result;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends Serializable, T> T[] aggregate(
            Collection<E>  entities,
            Function<E, T> function,
            Class<T>       aggregatedType
    ) {
        T[] aggregated;
        int i;

        aggregated = (T[])newInstance(aggregatedType, entities.size());
        i = 0;
        for(E entity : entities)
            aggregated[i++] = function.apply(entity);

        return aggregated;
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