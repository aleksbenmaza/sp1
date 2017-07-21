package app.core.business.model.mapping;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Table(name = "modeles")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Model extends IdentifiableByIdImpl {

    @Column(name = "nom")
    private String name;

    @Column(name = "annee")
    private short year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marque_id", referencedColumnName = "id")
    private Make make;

    @OneToMany(mappedBy = "model", cascade  = CascadeType.ALL)
    private Set<Vehicle> vehicles;

    public Model(Make make) {
        vehicles  = new HashSet<Vehicle>();
        this.make = make;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Make getMake(){
        return make;
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<Vehicle>(vehicles);
    }

    public boolean addVehicle(Vehicle vehicle) {
        return vehicles.add(vehicle);
    }

    Model() {
    }
}
