package org.aaa.core.web.common.business.repository;

import static org.aaa.util.ObjectUtils.replaceIfNull;

import org.aaa.core.business.mapping.*;


import org.aaa.core.business.repository.DAO;
import org.hibernate.*;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by alexandremasanes on 21/02/2017.
 */

@SuppressWarnings("unchecked")
@Repository("dao")
public final class DAOImpl implements DAO {

    @Value("#{@sessionFactory.openSession()}")
    private Session session;

    private String hashSalt;

    private short  tokenLifetime;

    @Override
    public void save(Entity entity) {
        commitOrRollback(this::saveAndFlush, entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> find(Class<T> entityClass) {
        Query<T> query;
        query = session.createQuery("FROM " + entityClass.getSimpleName());
        return query.list();
    }

    @Override
    public boolean remove(Entity entity) {
        return replaceIfNull(commitOrRollback(this::removeAndFlush, entity), false);
    }

    @Override
    public boolean trackBack(Entity entity) {
        if(session.contains(entity))
            return false;
        session.merge(entity);
        return true;
    }


    @Override
    public <T extends Entity> Map<T, Boolean> remove(Set<T> entities) {

        HashMap<T, Boolean> map = new HashMap<>();
        for(T entity : entities)
            map.put(entity, remove(entity));

        return map;
    }


    @Override
    public <T extends IdentifiableByIdImpl> List<T> find(Class<T> entityClass, long... ids) {
        Query<T> query;
        query = session.createQuery("FROM "+entityClass.getSimpleName());
        return query.list();
    }

    @Override
    public <T extends IdentifiableByIdImpl> T find(Class<T> entityClass, long id) {
        Query<T> query;
        query = session.createQuery("FROM "+entityClass.getSimpleName()+" " +
                "WHERE id = :id");
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public List<Make> searchMakes(String name) {
        Query<Make> query;
        query = session.createQuery("FROM " + Make.class.getSimpleName() + " " +
                "WHERE name LIKE :name");
        query.setParameter("name", name+"%");
        return query.list();
    }

    @Override
    public List<Model> searchModels(String name) {
        Query<Model> query;
        query = session.createQuery("FROM " + Model.class.getSimpleName() + " " +
                "WHERE name LIKE :name");
        query.setParameter("name", name+"%");
        return query.list();
    }

    @Override
    public List<Model> searchModels(String modelName, long makeId) {
        Query<Model> query;
        String stm = "FROM " + Model.class.getSimpleName() + " " +
                     "WHERE make.id = :id " +
                     "AND   name LIKE :name";
        query = session.createQuery(stm);
        query.setParameter("id", makeId)
             .setParameter("name", modelName + "%");
        return query.list();
    }

    @Override
    public boolean refresh(IdentifiableByIdImpl entity) {
        Query  query;
        Object refreshedEntity;
        try {
            query = session.createQuery(
                    "FROM " + entity.getClass().getSimpleName() +
                       "WHERE id = " + entity.getId()
            );

            refreshedEntity = query.uniqueResult();
            session.detach(refreshedEntity);
            session.clear();
            copyProperties(refreshedEntity, entity);
            return true;
        } catch(StackOverflowError e) {
            System.out.println("S.O");
            return false;
        }
    }

    @Override
    public <T extends IdentifiableByIdImpl> boolean has(Class<T> entityClass, long id) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE COUNT(e) > 0 THEN TRUE ELSE FALSE END " +
              "FROM " + entityClass.getSimpleName() + " e " +
              "WHERE id = :id";
        query = session.createQuery(stm);
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
        query = session.createQuery(stm);
        query.setParameter("email", email);

        return query.uniqueResult();
    }

    @Override
    public float computeDeductibleValue(long insuranceId, float damageAmount) {
        String stm;
        Query<Float> query;

        stm   = "SELECT compute_deductible(:insuranceId, :damageAmount)";
        query = session.createNativeQuery(stm);
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
        query = session.createQuery(stm);

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
        query = session.createQuery(stm);

        query.setParameter("email", email);

        return query.uniqueResult();
    }

    @Override
    public Vehicle findVehicle(String registrationNumber) {
        Query<Vehicle> query;

        String stm = "FROM " + Vehicle.class.getSimpleName() + " " +
                     "WHERE registrationNumber = :registrationNumber";

        query = session.createQuery(stm);

        query.setParameter("registrationNumber", registrationNumber);

        return query.uniqueResult();
    }

    @Override
    public Model findModel(String name) {
        String stm;
        Query<Model> query;

        stm = "FROM " + Model.class.getSimpleName() + " WHERE name = :name";

        query = session.createQuery(stm);

        query.setParameter("name", name);

        return query.uniqueResult();
    }

    @Override
    public <T extends IdentifiableByIdImpl> long getNextId(Class<T> entityClass) {
        NativeQuery<Long> query;
        String stm = "SELECT get_next_id('" + entityClass.getAnnotation(Table.class).name() + "')";

        query = session.createNativeQuery(stm);

        return query.uniqueResult();
    }

    @Override
    public boolean hasMake(String name) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
                "FROM " + Make.class.getSimpleName() + " m " +
                "WHERE name = :name";

        query = session.createQuery(stm);

        query.setParameter("name", name);

        return query.uniqueResult();

    }

    @Override
    public boolean hasModel(String name) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
                "FROM " + Model.class.getSimpleName() + " m " +
                "WHERE name = :name";

        query = session.createQuery(stm);

        query.setParameter("name", name);

        return query.uniqueResult();
    }

    @Override
    public boolean hasToken(String tokenValue) {
        String stm;
        Query<Boolean> query;

        stm = "SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END " +
                "FROM " + Token.class.getSimpleName() + " t " +
                "WHERE value = :tokenValue";
        query = session.createQuery(stm);
        query.setParameter("tokenValue", tokenValue);

        return query.uniqueResult();
    }

    @Override
    public Token findToken(String tokenValue) {
        String stm;
        Query<Token> query;

        stm = "FROM " + Token.class.getSimpleName() + " " +
                "WHERE value = :tokenValue";

        query = session.createQuery(stm);
        query.setParameter("tokenValue", tokenValue);
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

        stm = "SELECT get_hash_salt(), get_token_lifetime()";

        query = session.createNativeQuery(stm);

        values = query.uniqueResult();

        hashSalt      = (String) values[0];
        tokenLifetime = (
                (Integer) replaceIfNull(values[1], 0)
        ).shortValue();

    }

    @PreDestroy
    protected void destroy() {
        session.getSessionFactory().close();
        session.close();
    }


    private void commitOrRollback(Runnable runnable) {
        Transaction tx;
        tx = session.beginTransaction();
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

        tx = session.beginTransaction();
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
        tx = session.beginTransaction();
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

    private boolean removeAndFlush(Entity entity) {
        if(!session.contains(entity))
            return false;
        session.delete(entity);
        return true;
    }

    private void saveAndFlush(Entity entity) {
        session.saveOrUpdate(entity);
        session.flush();
    }

    private <T> void commitOrRollback(Consumer<T> consumer, T parameter) {
        Transaction tx;
        tx = session.beginTransaction();
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