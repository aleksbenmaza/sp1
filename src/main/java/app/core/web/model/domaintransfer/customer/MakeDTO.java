package app.core.web.model.domaintransfer.customer;

import app.core.business.model.mapping.Make;
import app.core.web.model.domaintransfer.Presentation;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class MakeDTO extends Presentation<Make> {

    private long id;

    private String name;

    public MakeDTO(Make make) {
        super(make);

        id   = make.getId();
        name = make.getName();

    }

}
