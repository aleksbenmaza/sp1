package org.aaa.core.business.mapping.entity;


import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

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
@Table(name = "makes")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Make extends IdentifiedByIdEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "make", cascade = CascadeType.ALL)
    private Set<Model> models;

    public Make() {
        models = new HashSet<>();
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
        return new HashSet<>(models);
    }
}
