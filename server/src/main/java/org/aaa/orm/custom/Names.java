package org.aaa.orm.custom;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 18/11/2017.
 */

public class Names implements Serializable {

    private static final long serialVersionUID = 1452317376761913273L;

    private String firstName;

    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Names names = (Names) o;

        if (firstName != null ? !firstName.equals(names.firstName) : names.firstName != null) return false;
        return lastName != null ? lastName.equals(names.lastName) : names.lastName == null;
    }

}
