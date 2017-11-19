package org.aaa.core.business.mapping.entity;

import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 23/09/2017.
 */
@Entity
@Table(name = "years")
public class Year extends IdentifiedByIdEntity {

    private static final long serialVersionUID = -683393275054120818L;

    @Column(unique = true)
    private short value;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name               = "models__years",
            joinColumns        = @JoinColumn(
                    name                 = "year_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name                 = "model_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Model> models;

    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles;

    public Year(short value) {
        this.value = value;
        models   = new HashSet<>();
        vehicles = new HashSet<>();
    }

    public short getValue() {
        return value;
    }

    public Set<Model> getModels() {
        return new HashSet<>(models);
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    public boolean addModel(Model model) {
        return !model.getYears().contains(this) && models.add(model);
    }

    public boolean addVehicle(Vehicle vehicle) {
        check(models.contains(vehicle.getModel()));
        return vehicles.add(vehicle);
    }

    @Override
    public final int hashCode() {
        return Short.hashCode(value);
    }

    Year() {}
}
