package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.entity.Model;
import org.aaa.core.business.mapping.entity.Year;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

import static org.apache.commons.lang.ArrayUtils.toPrimitive;

import java.util.Set;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class ModelDTO extends DTO<Model> {

    private static final long serialVersionUID = -50575673022228162L;

    private long id;

    private String name;

    @SerializedName("make_id")
    private long makeId;

    private short[] years;


    public ModelDTO(Model model) {
        super(model);
        Set<Year> years;
        int i;

        years = model.getYears();
        this.years = toPrimitive(
                aggregate(
                        years,
                        Year::getValue,
                        Short.class
                )
        );

        id     = model.getId();
        name   = model.getName();
        makeId = model.getMake().getId();
    }
}
