package org.aaa.core.business.repository;

import org.aaa.core.business.mapping.Entity;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface Recorder {

    void save(Entity entity);

//    void save(Token token);
}
