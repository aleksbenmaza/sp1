package org.aaa.core.business.mapping.entity;

import org.aaa.core.business.mapping.entity.damage.Spoilage;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 20/03/2017.
 */

@Entity
@Table(name = "car_dealers")
public class CarDealer extends IdentifiedByIdEntity {

    private static final long serialVersionUID = 8027881639510234611L;

    @Column
    private String name;

    @Column
    private boolean certified;

    @OneToMany(mappedBy = "carDealer", cascade = CascadeType.ALL)
    private Set<Spoilage> spoilages;

    {
        spoilages = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public Set<Spoilage> getSpoilages() {
        return new HashSet<Spoilage>(spoilages);
    }

    public boolean addSpoilage(Spoilage spoilage) {
        return spoilages.add(spoilage);
    }
}