package org.aaa.core.web.api.model.ouput.customer.sinister;

import com.google.gson.annotations.JsonAdapter;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.web.api.model.input.databinding.sinister.GsonPolymorphicAdapter;
import org.aaa.core.web.api.model.ouput.DTO;

import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
@JsonAdapter(GsonPolymorphicAdapter.class)
public abstract class SinisterDTO extends DTO<Sinister> {

    protected long id;

    protected Date date;

    protected Time time;

    protected String comment;

    protected boolean closed;

    public SinisterDTO(Sinister sinister) {
        super(sinister);
        id      = sinister.getId();
        date    = sinister.getDate();
        time    = sinister.getTime();
        comment = sinister.getComment();
        closed  = sinister.isClosed();
    }
}
