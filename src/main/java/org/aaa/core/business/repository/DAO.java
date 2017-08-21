package org.aaa.core.business.repository;

import org.aaa.core.business.mapping.*;
import org.aaa.core.business.mapping.Token;

import java.util.List;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface DAO extends Finder, Recorder, Remover {

    boolean refresh(IdentifiableByIdImpl entity);

    boolean trackBack(Entity entity);

    List<Make> searchMakes(String name);

    List<Model> searchModels(String name);

    List<Model> searchModels(String modelName, long makeId);

    Vehicle findVehicleByRegistrationNumber(String registrationNumber);

    UserAccount findUserAccount(String email, String hash);

    float computeDeductibleValue(long insuranceId, float damageAmount);

    UserAccount findUserAccount(String email);

    boolean emailExists(String email);

    Token findToken(String value);

    boolean tokenExists(String token);

    String getHashSalt();

    int getTokenLifetime();
}
