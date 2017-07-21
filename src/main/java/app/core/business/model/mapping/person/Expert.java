package app.core.business.model.mapping.person;

import app.core.business.model.mapping.Coverage;
import app.core.business.model.mapping.UserAccount;

import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Table(name = "experts")
public class Expert extends Person implements RegisteredUser {

    @Column(name = "rang")
    protected Integer rank;

    @OneToMany(mappedBy = "expert",
               cascade  = {CascadeType.PERSIST, CascadeType.REMOVE})
    private transient Set<Coverage> coverages;

    public Expert(){
        coverages = new HashSet<Coverage>();
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

    public Set<Coverage> getCoverages() {
        return new HashSet<Coverage>(coverages);
    }

    public boolean addCoverage(Coverage coverage) {
        return coverages.add(coverage);
    }

    public boolean removeCoverage(Coverage coverage) {
        return coverages.remove(coverage);
    }
}