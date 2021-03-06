package org.aaa.core.business.mapping.entity.person;

import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.mapping.entity.sinister.Sinister;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Table(name = "experts")
public class Expert extends Person implements RegisteredUser {

    private static final long serialVersionUID = -8210740393208610872L;

    @Column
    protected Integer rank;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private Set<Sinister> sinisters;

    public Expert(String nirNumber) {
        super(nirNumber);
        sinisters = new HashSet<>();
    }

    @Override
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Set<Sinister> getSinisters() {
        return new HashSet<>(sinisters);
    }

    public boolean addSinister(Sinister sinister) {
        return sinisters.add(sinister);
    }

    Expert() {}
}