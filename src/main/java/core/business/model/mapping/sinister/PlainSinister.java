package core.business.model.mapping.sinister;


import core.business.model.mapping.IdentifiableByIdImpl;
import core.business.model.mapping.Insurance;

import javax.persistence.*;
import javax.persistence.Entity;

import core.business.model.mapping.Vehicle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "sinistres_sans_tiers")
public class PlainSinister extends Sinister {

    @Entity
    @Table(name  = "types_sinistres_sans_tiers")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public static class Type extends IdentifiableByIdImpl {

        @Column(unique = true)
        private String code;

        @Column(name = "libelle", unique = true)
        private String name;

        @OneToMany(cascade = CascadeType.ALL)
        private Set<PlainSinister> plainSinisters;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name               = "types_sinistres_types_garantie",
                   joinColumns        = {@JoinColumn(name                 = "type_sinistres_id",
                                                     referencedColumnName = "id")},
                   inverseJoinColumns = {@JoinColumn(name                 = "type_garantie_id",
                                                     referencedColumnName = "id")})
        private Set<Insurance> insurances;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;

    public PlainSinister(Vehicle vehicle, Type type) {
        super(vehicle);
        type.plainSinisters.add(this);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    PlainSinister() {
    }
}