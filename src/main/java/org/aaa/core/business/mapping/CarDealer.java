package org.aaa.core.business.mapping;

import org.aaa.core.business.mapping.damage.Spoilage;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 20/03/2017.
 */

@Entity
@Table(name = "car_dealers")
public class CarDealer extends IdentifiableByIdImpl {

    @Column
    private String name;

    @Column
    private boolean certified;

    @OneToMany(mappedBy = "carDealer", cascade = CascadeType.ALL)
    private Set<Spoilage> spoilages;

    public CarDealer() {
        spoilages = new HashSet<Spoilage>();
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