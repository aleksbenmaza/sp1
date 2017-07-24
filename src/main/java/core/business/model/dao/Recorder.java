package app.core.business.model.dao;

import app.core.business.model.mapping.Entity;
import app.core.business.model.mapping.Token;

import java.util.Set;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Recorder {

    void save(Entity entity);

//    void save(Token token);
}
