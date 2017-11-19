package org.aaa.core.web.api.model.ouput.customer;

import org.aaa.core.business.mapping.entity.Make;
import org.aaa.core.web.api.model.ouput.DTO;

/**
 * Created by alexandremasanes on 13/04/2017.
 */
public class MakeDTO extends DTO<Make> {

    private static final long serialVersionUID = 3924640363807625089L;

    private long id;

    private String name;

    public MakeDTO(Make make) {
        super(make);

        id   = make.getId();
        name = make.getName();

    }

}
