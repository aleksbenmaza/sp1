package core.business.model.mapping;


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
@Table(name = "marques")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Make extends IdentifiableByIdImpl {

    @Column(name = "nom")
    private String name;

    @OneToMany(mappedBy = "make", cascade = CascadeType.ALL)
    private Set<Model> models;

    public Make() {
        models = new HashSet<Model>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addModel(Model model) {
        return models.add(requireNonNull(model));
    }

    public Set<Model> getModels() {
        return new HashSet<Model>(models);
    }
}
