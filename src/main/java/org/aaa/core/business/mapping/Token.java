package org.aaa.core.business.mapping;


import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 28/04/2017.
 */
@Entity
@Table(name = "tokens")
public class Token extends IdentifiableByIdImpl {

    @Column
    private String value;

    @Column(name = "old_value")
    private String oldValue;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at")
    private Timestamp createdAt;

    //Token has to be the inverse-side, if not Hbn will not properly fetch UserAccount
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name 		       = "tokens__user_accounts",
            joinColumns 	   = @JoinColumn(
                    name                 = "token_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name                 = "user_account_id",
                    referencedColumnName = "id"
            )
    ) private UserAccount userAccount;

    public Token() {}

    public Token(UserAccount userAccount) {
        setUserAccount(requireNonNull(userAccount));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }


    public Timestamp createdAt() {
        return createdAt;
    }

    @Nullable
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(@Nullable UserAccount userAccount) {
        this.userAccount = userAccount;
        if(userAccount != null && userAccount.getToken() != this)
            userAccount.setToken(this);
    }
}
