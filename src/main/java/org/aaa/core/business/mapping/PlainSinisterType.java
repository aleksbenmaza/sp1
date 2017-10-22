package org.aaa.core.business.mapping;

import org.aaa.core.business.mapping.sinister.PlainSinister;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexandremasanes on 08/10/2017.
 */
@Entity
@Table(name = "plain_sinister_types")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlainSinisterType extends IdentifiedByIdEntity {

    @Column(
            unique = true
    )
    private String code;

    @Column(
            unique = true
    )
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "type"
    )
    private Set<PlainSinister> plainSinisters;

    @ElementCollection
    @CollectionTable(
            name = "plain_sinister_types__insurances",
            joinColumns = @JoinColumn(
                    name = "plain_sinister_type_id",
                    referencedColumnName = "id"
            )
    )
    @MapKeyJoinColumn(name = "insurance_id", referencedColumnName = "id")
    private Map<Insurance, Coverage> coveragesByInsurance;

    public PlainSinisterType() {
        plainSinisters = new HashSet<>();
        coveragesByInsurance = new HashMap<>();
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

    public Map<Insurance, Coverage> getCoveragesByInsurance() {
        return (coveragesByInsurance);
    }

    public boolean putCoverageByInsurance(Insurance insurance, Coverage coverage) {
        if (coveragesByInsurance.containsKey(insurance))
            return false;
        coveragesByInsurance.put(insurance, coverage);
        insurance.putCoverageBySinisterType(this, coverage);
        return true;
    }
}
