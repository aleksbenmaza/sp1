package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.Model;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class ModelDTO extends DTO<Model> {

    private long id;

    private String name;

    private short year;

    @SerializedName("make_id")
    private long makeId;


    public ModelDTO(Model model) {
        super(model);
        id     = model.getId();
        name   = model.getName();
        year   = model.getYear();
        makeId = model.getMake().getId();
    }
}
