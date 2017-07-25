package core.web.api.model.ouput.customer.sinister;

import core.business.model.mapping.sinister.Sinister;
import core.web.api.model.ouput.Presentation;

import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public abstract class SinisterDTO extends Presentation<Sinister> {

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
