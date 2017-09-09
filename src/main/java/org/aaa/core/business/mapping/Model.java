package org.aaa.core.business.mapping;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Immutable
@Table(name = "models")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Model extends IdentifiableByIdImpl {

    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "make_id", referencedColumnName = "id", nullable = false)
    private Make make;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private Set<ModelAndYear> modelsAndYears;

    public Model(Make make) {
        modelsAndYears  = new HashSet<>();
        this.make = requireNonNull(make);
        make.addModel(this);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Make getMake(){
        return make;
    }

    public Set<ModelAndYear> getModelsAndYears() {
        return new HashSet<>(modelsAndYears);
    }

    public boolean addModelAndYear(ModelAndYear modelAndYear) {
        return modelsAndYears.add(modelAndYear);
    }

    Model() {}
}
