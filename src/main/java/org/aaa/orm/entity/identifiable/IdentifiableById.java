package org.aaa.orm.entity.identifiable;

import java.util.*;

/**
 * Created by alexandremasanes on 05/05/2017.
 */
public interface IdentifiableById {

    long NULL_ID = 0;

    Comparator<IdentifiableById> comparator = (o1, o2) -> {
        if (o1 == o2)
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return +1;
        if (o1.getId() == o2.getId())
            return 0;
        return o1.getId() > o2.getId() ? +1 : -1;
     };


    static <T extends IdentifiableById> List<T> toSortedList(Set<T> identifiableByIds) {
        ArrayList<T> resultList;
        resultList = new ArrayList<>(identifiableByIds);
        resultList.sort(comparator);
        return resultList;
    }

    long getId();

    @Override
    int hashCode();
}