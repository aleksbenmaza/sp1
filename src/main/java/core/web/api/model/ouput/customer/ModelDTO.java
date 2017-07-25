package core.web.api.model.ouput.customer;

import core.business.model.mapping.Model;
import core.web.api.model.ouput.Presentation;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class ModelDTO extends Presentation<Model> {

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
