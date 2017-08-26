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

    Vehicle findVehicle(String registrationNumber);

    UserAccount findUserAccount(String email, String hash);

    float computeDeductibleValue(long insuranceId, float damageAmount);

    UserAccount findUserAccount(String email);

    Model findModel(String name);

    boolean hasMake(String name);

    boolean hasModel(String name);

    boolean hasUserAccount(String email);

    Token findToken(String value);

    boolean hasToken(String token);

    String getHashSalt();

    short getTokenLifetime();
}
