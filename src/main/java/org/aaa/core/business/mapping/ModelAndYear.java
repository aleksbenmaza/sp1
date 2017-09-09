package org.aaa.core.business.mapping;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.aaa.util.MathUtils.digitCount;

/**
 * Created by alexandremasanes on 24/08/2017.
 */
@Entity
@Table(name = "models__years")
public class ModelAndYear implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;

    @Id
    private short year;

    @OneToMany(mappedBy = "modelAndYear", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles;

    public ModelAndYear(Model model, short year) {
        vehicles = new HashSet<>();
        this.model = model;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelAndYear that = (ModelAndYear) o;

        if (year != that.year) return false;
        return model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return  (model.hashCode() * (int) digitCount(Short.MAX_VALUE)) + year;
    }

    public Model getModel() {
        return model;
    }

    public short getYear() {
        return year;
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    public boolean addVehicle(Vehicle vehicle) {
        return vehicles.add(vehicle);
    }

    private ModelAndYear() {}
}
