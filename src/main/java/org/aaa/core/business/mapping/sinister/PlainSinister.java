package org.aaa.core.business.mapping.sinister;


import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.IdentifiableByIdImpl;
import org.aaa.core.business.mapping.Insurance;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "plain_sinisters")
public class PlainSinister extends Sinister {

    @Entity
    @Table(name  = "plain_sinister_types")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public static class Type extends IdentifiableByIdImpl {

        @Column(
                unique = true
        ) private String code;

        @Column(
                unique = true
        ) private String name;

        @OneToMany(
                cascade  = CascadeType.ALL,
                mappedBy = "type"
        ) private Set<PlainSinister> plainSinisters;

        @ManyToMany(
                cascade = CascadeType.ALL
        ) @JoinTable(
                name               = "plain_sinister_types__insurances",
                joinColumns        = @JoinColumn(
                        name                 = "plain_sinister_type_id",
                        referencedColumnName = "id"
                ),
                inverseJoinColumns = @JoinColumn(
                        name                 = "insurance_id",
                        referencedColumnName = "id"
                )
        ) private Set<Insurance> insurances;

        public Type() {
            plainSinisters = new HashSet<PlainSinister>();
            insurances     = new HashSet<Insurance>();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<PlainSinister> getPlainSinisters() {
            return plainSinisters;
        }

        public Set<Insurance> getInsurances() {
            return new HashSet<Insurance>(insurances);
        }

        public boolean addInsurance(Insurance insurance) {
            if(insurances.contains(insurance))
                return false;
            insurances.add(insurance);
            insurance.addSinisterType(this);
            return true;
        }
    }

    @ManyToOne(
            cascade = CascadeType.ALL
    ) @JoinColumn(
            name                 = "plain_sinister_type_id",
            referencedColumnName = "id"
    ) private Type type;

    public PlainSinister(Contract contract, Type type) {
        super(contract);
        this.type = requireNonNull(type);
        type.plainSinisters.add(this);
    }


    public Type getType() {
        return type;
    }

    PlainSinister() {}
}