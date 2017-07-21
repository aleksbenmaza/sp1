package app.core.business.model.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

/**
 * Created by alexandremasanes on 05/05/2017.
 */
public interface IdentifiableById {

    long NULL_ID = 0;

    final class Utils {
        private final static class ComparatorImpl<T extends IdentifiableById> implements Comparator<IdentifiableById> {
            @Override
            public int compare(IdentifiableById o1, IdentifiableById o2) {

                if(o1 == o2 || (o1 != null && o2 != null  && ( o1.equals(o2) || o1.getId() == o2.getId())))
                    return 0;

                if(o2 == null || (o1 != null && o1.getId() > o2.getId()))
                    return 1;

                return -1;
            }
        }

        public static <T extends IdentifiableById> List<T> toSortedList(Set<T> identifiableByIds) {
            ArrayList<T> resultList;
            resultList = new ArrayList<>(identifiableByIds);
            resultList.sort(new ComparatorImpl<>());
            return resultList;
        }

        Utils(){
        }
    }

    long getId();

    int hashCode();

    default boolean equals(IdentifiableById o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentifiableById identifiableById = (IdentifiableById) o;

        return getId() != NULL_ID ? getId() == o.getId() : o.getId() == NULL_ID;
    }
}