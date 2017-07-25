package core.web.api.model.ouput.customer;

import core.business.model.mapping.Make;
import core.web.api.model.ouput.Presentation;

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
