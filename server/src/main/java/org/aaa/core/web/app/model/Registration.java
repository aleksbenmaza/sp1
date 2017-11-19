package org.aaa.core.web.app.model;

import org.springframework.web.multipart.MultipartFile;


/**
 * Created by alexandremasanes on 20/02/2017.
 */

public final class Registration extends Command {

    private static final long serialVersionUID = 3020780761603956568L;

    private String emailAddress;

    private String password;

    private String passwordConfirm;

    private String lastName;

    private String firstName;

    private String address;

    private Short zipCode;

    private String city;

    private String phoneNumber;

    private String nirNumber;

    private MultipartFile idCard;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Short getZipCode() {
        return zipCode;
    }

    public void setZipCode(Short zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MultipartFile getIdCard() {
        return idCard;
    }

    public void setIdCard(MultipartFile idCard) {
        this.idCard = idCard;
    }

    public String getNirNumber() {
        return nirNumber;
    }

    public void setNirNumber(String nirNumber) {
        this.nirNumber = nirNumber;
    }

    @Override
    public boolean isEmpty() {
        return (passwordConfirm == null || passwordConfirm.isEmpty())
                &&
               (lastName        == null || lastName.isEmpty())
                &&
               (firstName       == null || firstName.isEmpty())
                &&
               (address         == null || address.isEmpty())
                &&
                zipCode         == null
                &&
               (city            == null || city.isEmpty() )
                &&
               (phoneNumber     == null || phoneNumber.isEmpty())
                &&
               (idCard          == null || idCard.isEmpty());
    }

    @Override
    public String toString() {
        return "Registration{" +
                "emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address='" + address + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", idCard=" + idCard +
                '}';
    }
}