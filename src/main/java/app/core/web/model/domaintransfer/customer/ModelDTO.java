package app.core.web.model.domaintransfer.customer;

import app.core.business.model.mapping.Model;
import app.core.web.model.domaintransfer.Presentation;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;

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
