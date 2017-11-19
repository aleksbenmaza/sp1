package org.aaa.core.business.mapping.entity;


import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by alexandremasanes on 28/04/2017.
 */
@Entity
@Table(
        name = "tokens",
        uniqueConstraints =
            @UniqueConstraint(
                    columnNames = "value"
            )
)
public class Token extends IdentifiedByIdEntity {

    private static final long serialVersionUID = -534311138982607094L;

    @Column
    private UUID value;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at")
    private Timestamp createdAt;

    //Token has to be the inverse-side, if not Hbn will not properly fetch UserAccount
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Token(UUID value) {
        this.value = value;
    }

    public Token(UUID value, UserAccount userAccount) {
        this(value);
        this.userAccount = userAccount;
        setUserAccount(requireNonNull(userAccount));
    }

    public UUID getValue() {
        return value;
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

    @Override
    public final int hashCode() {
        return value.hashCode();
    }

    Token() {}
}
