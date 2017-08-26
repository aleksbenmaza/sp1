package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.Model;
import org.aaa.core.business.mapping.ModelAndYear;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

import static org.apache.commons.lang.ArrayUtils.toPrimitive;

import java.util.Set;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class ModelDTO extends DTO<Model> {

    private long id;

    private String name;

    @SerializedName("make_id")
    private long makeId;

    private short[] years;


    public ModelDTO(Model model) {
        super(model);
        Set<ModelAndYear> modelsAndYears;
        int i;

        modelsAndYears = model.getModelsAndYears();
        years = toPrimitive(
                aggregate(
                        modelsAndYears,
                        ModelAndYear::getYear,
                        Short.class
                )
        );

        id     = model.getId();
        name   = model.getName();
        makeId = model.getMake().getId();
    }
}
