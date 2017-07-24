package core.web.model.domaintransfer;

import core.business.model.mapping.Entity;
import core.business.model.mapping.IdentifiableById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public abstract class Presentation<E extends Entity> implements Serializable {

    public Presentation(E entity) {
        if(entity.updatedAt() == null)
            throw new RuntimeException("Entity must be persisted before !");
    }

    public static <E extends Entity, P extends  Presentation<E>> List<P> fromCollection(
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
