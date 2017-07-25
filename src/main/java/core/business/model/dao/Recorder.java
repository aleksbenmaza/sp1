package core.business.model.dao;

import core.business.model.mapping.Entity;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Recorder {

    void save(Entity entity);

//    void save(Token token);
}
