package org.aaa.orm.custom;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.postgresql.util.PGobject;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by alexandremasanes on 18/11/2017.
 */
public class NamesType implements CompositeUserType {



    @Override
    public Class returnedClass() {
        return Names.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return  o == o1 || o != null && o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        assert strings.length == 2;

        String firstName;
        String lastName;

        Names names;

        firstName = resultSet.getObject(strings[0]).toString();
        lastName  = resultSet.getObject(strings[1]).toString();

        if(firstName == null && lastName == null)
            return null;

        names = new Names();

        names.setFirstName(firstName);
        names.setLastName(lastName);

        return names;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        Names names;
        PGobject pgObject;


        if (o != null)  {
            names = (Names) o;
            pgObject = new PGobject();

            pgObject.setType("names");
            pgObject.setValue("ROW('" + names.getFirstName() + "','" + names.getLastName() + ") ");

            preparedStatement.setObject(i, names.getFirstName());
            preparedStatement.setObject(i, names.getLastName());
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        Names original;
        Names copy;

        original = (Names) o;
        copy     = new Names();

        copy.setFirstName(original.getFirstName());
        copy.setLastName(original.getLastName());

        return copy;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "first_name",
                "last_name"
        };
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[] {
                StringType.INSTANCE,
                StringType.INSTANCE
        };
    }

    @Override
    public Object getPropertyValue(Object o, int i) throws HibernateException {
        Names names;

        names = (Names) o;
        return i == 0 ? names.getFirstName() : names.getLastName();
    }

    @Override
    public void setPropertyValue(Object o, int i, Object o1) throws HibernateException {
        Names names;

        names = (Names) o;
        switch (i) {
            case 0:
                names.setFirstName((String) o1);
                break;
            case 1:
                names.setLastName((String) o1);
                break;
            default:
                throw new HibernateException("");
        }

    }

    @Override
    public Serializable disassemble(Object o, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        return (Serializable) o;
    }

    @Override
    public Object assemble(Serializable serializable, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return serializable;
    }

    @Override
    public Object replace(Object o, Object o1, SharedSessionContractImplementor sharedSessionContractImplementor, Object o2) throws HibernateException {
        return o;
    }
}