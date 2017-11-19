package org.aaa.orm.custom;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 18/11/2017.
 */

public class Address implements Serializable {

    private static final long serialVersionUID = -350326960015294554L;

    private short number;

    private boolean isBis;

    private String  street;

    private int zipCode;

    private String city;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public boolean isBis() {
        return isBis;
    }

    public void setBis(boolean bis) {
        isBis = bis;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
