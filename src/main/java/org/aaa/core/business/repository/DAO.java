package org.aaa.core.business.repository;

import org.aaa.core.business.mapping.entity.*;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.sinister.Sinister;
import org.aaa.core.business.mapping.view.VehicleCountByInsureeAndModel;
import org.aaa.orm.entity.BaseEntity;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import java.util.List;
import java.util.UUID;

/**
 * Created by alexandremasanes on 21/02/2017.
 */
public interface DAO extends Finder, Recorder, Remover {

    <T extends IdentifiedByIdEntity> boolean refresh(T entity);

    <T extends BaseEntity> boolean trackBack(T entity);

    List<Make> searchMakes(String name);

    List<Model> searchModels(String name);

    List<Model> searchModels(String modelName, long makeId);

    Vehicle findVehicle(String registrationNumber);

    UserAccount findUserAccount(String email, String hash);

    float computeDeductibleValue(long insuranceId, float damageAmount);

    UserAccount findUserAccount(String email);

    Make findMake(String name);

    Model findModel(String name);

    Year findYear(short value);

    boolean hasMake(String name);

    boolean hasModel(String name);

    boolean hasYear(short value);

    boolean hasUserAccount(String emailAddress);

    Token findToken(UUID tokenValue);

    Contract findContract(Customer customer, long contractId);

    Sinister findSinister(Customer customer, long sinisterId);

    boolean hasToken(UUID tokenValue);

    String getHashSalt();

    short getTokenLifetime();

    List<VehicleCountByInsureeAndModel> getVehicleCountByInsureeAndModel();
}
