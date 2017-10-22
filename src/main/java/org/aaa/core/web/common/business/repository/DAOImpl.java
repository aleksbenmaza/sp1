package org.aaa.core.web.common.business.repository;


import org.aaa.core.business.mapping.*;

import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.business.mapping.sinister.Sinister;
import org.aaa.core.business.repository.DAO;
import org.aaa.orm.entity.BaseEntity;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.orm.entity.UpdatableEntity;
import org.aaa.util.ObjectUtils;

import org.hibernate.*;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import static org.apache.commons.lang.ArrayUtils.toObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.Table;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by alexandremasanes on 21/02/2017.
 */

@SuppressWarnings("unchecked")
@Repository("dao")
public final class DAOImpl extends Object implements DAO {

    @Autowired
    private SessionFactory sessionFactory;

    private String hashSalt;

    private short  tokenLifetime;

    @Override
    public void save(UpdatableEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> find(Class<T> entityClass) {
        Query<T> query;
        query = getCurrentSession().createQuery("FROM " + entityClass.getSimpleName());
        return query.list();
    }

    @Override
    public boolean remove(UpdatableEntity entity) {
        if(getCurrentSession().contains(entity))
            return false;
        getCurrentSession().delete(entity);
        return true;
        //return ifNull(commitOrRollback(this::removeAndFlush, entity), false);
    }

    @Override
    public <T extends IdentifiedByIdEntity> boolean remove(Class<T> entityClass, long id) {
        return remove(entityClass, new long[] {id});
    }

    @Override
    public <T extends IdentifiedByIdEntity> boolean remove(Class<T> entityClass, long... ids) {
        String stm;
        Query<T> query;

        stm = "DELETE FROM " + entityClass.getSimpleName() + " " +
              "WHERE id IN :ids";
        query = getCurrentSession().createQuery(stm);
        query.setParameterList("ids", asList(toObject(ids)));

        return query.executeUpdate() == ids.length;
    }

    @Override
    public boolean trackBack(BaseEntity entity) {
        if(getCurrentSession().contains(entity))
            return false;
        getCurrentSession().merge(entity);
        return true;
    }


    @Override
    public <T extends UpdatableEntity> Map<T, Boolean> remove(Set<T> entities) {

        HashMap<T, Boolean> map = new HashMap<>();
        for(T entity : entities)
            map.put(entity, remove(entity));

        return map;
    }


    @Override
    public <T extends IdentifiedByIdEntity> List<T> find(Class<T> entityClass, long... ids) {
        String stm;
        Query<T> query;

        if(ids.length == 0)
            return find(entityClass);

        stm = "FROM " + entityClass.getSimpleName() + " " +
              "WHERE id IN :ids";

        query = getCurrentSession().createQuery(stm);

        query.setParameterList("ids", asList(toObject(ids)));
        return query.list();
    }

    @Override
    public <T extends IdentifiedByIdEntity> T find(Class<T> entityClass, long id) {
        Query<T> query;
        String stm;
        stm = "FROM " + entityClass.getSimpleName() + " " +
              "WHERE id = :id";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public List<Make> searchMakes(String name) {
        Query<Make> query;
        query = getCurrentSession().createQuery("FROM " + Make.class.getSimpleName() + " " +
                "WHERE name LIKE :name");
        query.setParameter("name", name+"%");
        return query.list();
    }

    @Override
    public List<Model> searchModels(String name) {
        Query<Model> query;
        String stm = "FROM " + Model.class.getSimpleName() + " " +
                     "WHERE name LIKE :name";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("name", name+"%");
        return query.list();
    }

    @Override
    public List<Model> searchModels(String modelName, long makeId) {
        Query<Model> query;
        String stm = "FROM " + Model.class.getSimpleName() + " " +
                     "WHERE make.id = :id " +
                     "AND   name LIKE :name";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("id", makeId)
             .setParameter("name", modelName + "%");
        return query.list();
    }

    @Override
    public <T extends IdentifiedByIdEntity> boolean refresh(T entity) {
        Query  query;
        Object refreshedEntity;
        String stm;

        stm = "FROM " + entity.getClass().getSimpleName() + " " +
              "WHERE id = " + entity.getId();
        try {
            query = getCurrentSession().createQuery(stm);
            refreshedEntity = query.uniqueResult();
            getCurrentSession().detach(entity);
            getCurrentSession().clear();
            copyProperties(refreshedEntity, entity);
            return true;
        } catch(StackOverflowError e) {
            System.out.println("S.O");
            return false;
        }
    }

    @Override
    public <T extends IdentifiedByIdEntity> boolean has(Class<T> entityClass, long id) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE COUNT(e) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + entityClass.getSimpleName() + " e " +
              "WHERE id = :id";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("id", id);

        return query.uniqueResult();
    }

    @Override
    public boolean hasUserAccount(String email) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(email) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + UserAccount.class.getSimpleName() + " " +
              "WHERE email = :email";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("email", email);

        return query.uniqueResult();
    }

    @Override
    public float computeDeductibleValue(long insuranceId, float damageAmount) {
        String stm;
        Query<Float> query;

        stm   = "SELECT compute_deductible(:insuranceId, :damageAmount)";
        query = getCurrentSession().createNativeQuery(stm);
        query.setParameter("insuranceId", insuranceId)
             .setParameter("damageAmount", damageAmount);

        return query.uniqueResult();
    }

    @Override
    public UserAccount findUserAccount(String emailAddress, String hash) {
        Query<UserAccount> query;
        UserAccount result;
        String stm = "FROM " + UserAccount.class.getSimpleName() + " " +
                     "WHERE emailAddress = :emailAddress " +
                     "AND   hash  = :hash ";
        query = getCurrentSession().createQuery(stm);

        query.setParameter("emailAddress", emailAddress);
        query.setParameter("hash", hash);

        return query.uniqueResult();
    }

    @Override
    public UserAccount findUserAccount(String email) {
        Query<UserAccount> query;
        UserAccount result;
        String stm = "FROM " + UserAccount.class.getSimpleName() + " " +
                     "WHERE email = :email ";
        query = getCurrentSession().createQuery(stm);

        query.setParameter("email", email);

        return query.uniqueResult();
    }

    @Override
    public Vehicle findVehicle(String registrationNumber) {
        Query<Vehicle> query;

        String stm = "FROM " + Vehicle.class.getSimpleName() + " " +
                     "WHERE registrationNumber = :registrationNumber";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("registrationNumber", registrationNumber);

        return query.uniqueResult();
    }

    @Override
    public Make findMake(String name) {
        String stm;
        Query<Make> query;

        stm = "FROM " + Make.class.getSimpleName() + " " +
              "WHERE LOWER(name) = :name";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }

    @Override
    public Model findModel(String name) {
        String stm;
        Query<Model> query;

        stm = "FROM " + Model.class.getSimpleName() + " " +
              "WHERE LOWER(name) = :name";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }

    @Override
    public Year findYear(short value) {
        String stm;
        Query<Year> query;

        stm = "FROM " + Year.class.getSimpleName() + " " +
                "WHERE value = :value";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("value", value);

        return query.uniqueResult();
    }

    @Override
    public <T extends IdentifiedByIdEntity> long getNextId(Class<T> entityClass) {
        Query<Long> query;
        String stm = "SELECT FUNCTION('get_next_id', '" + entityClass.getAnnotation(Table.class).name() + "')";

        query = getCurrentSession().createQuery(stm);

        return query.uniqueResult();
    }

    @Override
    public boolean hasMake(String name) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + Make.class.getSimpleName() + " m " +
              "WHERE LOWER(name) = :name";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();

    }

    @Override
    public boolean hasModel(String name) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + Model.class.getSimpleName() + " m " +
              "WHERE LOWER(name) = :name";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("name", name.toLowerCase());

        return query.uniqueResult();
    }

    @Override
    public boolean hasYear(short value) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
                "FROM " + Year.class.getSimpleName() + " m " +
                "WHERE value = :value";

        query = getCurrentSession().createQuery(stm);

        query.setParameter("value", value);

        return query.uniqueResult();
    }

    @Override
    public boolean hasToken(UUID tokenValue) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + Token.class.getSimpleName() + " t " +
              "WHERE value = :tokenValue";
        query = getCurrentSession().createQuery(stm);
        query.setParameter("tokenValue", tokenValue);

        return query.uniqueResult();
    }

    @Override
    public Token findToken(UUID tokenValue) {
        String stm;
        Query<Token> query;

        stm = "FROM " + Token.class.getSimpleName() + " " +
              "WHERE value = :tokenValue";

        query = getCurrentSession().createQuery(stm);
        query.setParameter("tokenValue", tokenValue);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    @Override
    public Contract findContract(Customer customer, long contractId) {
        String stm;
        Query<Contract> query;

        stm = "FROM " + Contract.class.getSimpleName() + " " +
              "WHERE customer = :customer AND id = :contractId";

        query = getCurrentSession().createQuery(stm);
        query.setParameter("customer", customer);
        query.setParameter("contractId", contractId);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    @Override
    public Sinister findSinister(Customer customer, long sinisterId) {
        String stm;
        Query<Sinister> query;

        stm = "FROM " + Sinister.class.getSimpleName() + " " +
              "WHERE contract.customer = :customer AND id = :sinisterId";

        query = getCurrentSession().createQuery(stm);
        query.setParameter("customer", customer);
        query.setParameter("sinisterId", sinisterId);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    @Override
    public String getHashSalt() {
        return hashSalt;
    }

    @Override
    public short getTokenLifetime() {
        return tokenLifetime;
    }

    @PostConstruct
    protected void init() {
        String stm;
        NativeQuery<Object[]> query;
        Object[] values;

        Session session;

        session = sessionFactory.openSession();

        stm = "SELECT get_hash_salt(), get_token_lifetime()";

        query = session.createNativeQuery(stm);

        values = query.uniqueResult();

        session.close();

        hashSalt      = (String) values[0];
        tokenLifetime = (
                (Integer) ObjectUtils.ifNull(values[1], 0)
        ).shortValue();

    }

    @PreDestroy
    protected void destroy() {
        sessionFactory.close();
    }

    private Session getCurrentSession() {
       return sessionFactory.getCurrentSession();
    }

    private void commitOrRollback(Runnable runnable) {
        Transaction tx;
        tx = getCurrentSession().beginTransaction();
        try {
            runnable.run();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    private <T, R> R commitOrRollback(Function<T, R> function, T parameter) {
        R result;
        Transaction tx;

        tx = getCurrentSession().beginTransaction();
        try {
            result = function.apply(parameter);
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    private <T, R> Object[] commitOrRollback(Runnable runnable, Function<T, R> function, T... parameters) {
        Transaction tx;
        tx = getCurrentSession().beginTransaction();
        Object[] results;
        int i;

        results = new Object[parameters.length];
        i = 0;

        try {
            runnable.run();
            for(T parameter : parameters)
                results[i++] = function.apply(parameter);
            tx.commit();
            return results;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    private <T, R> Object commitOrRollback(Runnable runnable, Function<T, R> function, T parameter) {
        return commitOrRollback(runnable, function, (T[])(new Object[]{parameter}))[0];
    }

    private boolean removeAndFlush(UpdatableEntity entity) {
        if(!getCurrentSession().contains(entity))
            return false;
        getCurrentSession().delete(entity);
        return true;
    }

    private void saveAndFlush(UpdatableEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().flush();
    }

    private <T> void commitOrRollback(Consumer<T> consumer, T parameter) {
        Transaction tx;
        tx = getCurrentSession().beginTransaction();
        try {
            consumer.accept(parameter);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }

    private <T> void copyProperties(T source, T target) {
        try {
            for (Field field : source.getClass().getFields()) {
                field.setAccessible(true);

                try {
                    field.set(target, field.get(source));

                } catch (IllegalAccessException e) {
                    System.err.println(e);
                }
            }
        } catch(StackOverflowError err) {
            System.out.println("S.O !");
        }
    }
}