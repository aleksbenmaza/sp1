package org.aaa.core.web.common.model.foreign.output.edmunds;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.business.mapping.entity.Make;
import org.aaa.core.web.common.model.foreign.output.ToEntitiesConvertible;

import java.util.Arrays;

/**
 * Created by alexandremasanes on 25/08/2017.
 */
public class MakesDTO implements ToEntitiesConvertible<Make> {

    private int makesCount;

    @SerializedName("makes")
    private MakeDTO[] makeDTOs;

    @Override
    public Make[] toEntities() {
        Make[] makes;
        int i;

        makes = new Make[makesCount];
        i = 0;

        for(MakeDTO makeDTO : makeDTOs)
            makes[i++] = makeDTO.toEntity();

        return makes;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "makesCount=" + makesCount +
                ", makes=" + Arrays.toString(makeDTOs) +
                '}';
    }

    public int getMakesCount() {
        return makesCount;
    }

    public MakeDTO[] getMakeDTOs() {
        return makeDTOs;
    }
}