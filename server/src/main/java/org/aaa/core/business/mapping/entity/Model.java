package org.aaa.core.business.mapping.entity;

import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Immutable
@Table(name = "models", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "make_id"}))
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Model extends IdentifiedByIdEntity {

    private static final long serialVersionUID = -1474458724416840144L;

    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "make_id", referencedColumnName = "id", nullable = false)
    private Make make;

    @ManyToMany(mappedBy = "models", cascade = CascadeType.ALL)
    private Set<Year> years;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles;

    public Model(String name, Make make) {
        years     = new HashSet<>();
        vehicles  = new HashSet<>();
        this.name = name;
        this.make = requireNonNull(make);
        make.addModel(this);
    }

    public String getName(){
        return name;
    }

    public Make getMake(){
        return make;
    }

    public Set<Year> getYears() {
        return new HashSet<>(years);
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    public boolean addYear(Year year) {
        return !year.getModels().contains(this) && years.add(year);
    }

    public boolean addVehicle(Vehicle vehicle) {
        check(years.contains(vehicle.getYear()));
        return vehicles.add(vehicle);
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(name)
                                    .append(make)
                                    .toHashCode();
    }

    Model() {}
}
