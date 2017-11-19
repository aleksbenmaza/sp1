package org.aaa.core.business.mapping.entity;

import org.aaa.core.business.mapping.embeddable.Coverage;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexandremasanes on 02/03/2017.
 */
@Entity
@Table(name = "insurances", uniqueConstraints ={@UniqueConstraint(columnNames = "code"), @UniqueConstraint(columnNames = "name")})
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Insurance extends IdentifiedByIdEntity {

    private static final long serialVersionUID = -7312824440761061655L;

    @Column(

    ) private String code;

    @Column(
    ) private String name;

    @Column(
            name = "default_amount"
    ) private float defaultAmount;

    @Column(
            name = "min_deductible"
    ) private double minDeductible;

    @Column(
            name = "max_deductible"
    ) private double maxDeductible;

    @OneToMany(
            mappedBy = "insurance",
            cascade  = CascadeType.ALL
    ) private Set<Contract> contracts;

    @ElementCollection
    @CollectionTable(
            name               = "plain_sinister_types__insurances",
            joinColumns = @JoinColumn(
                    name                 = "insurance_id",
                    referencedColumnName = "id"
            )
    ) @MapKeyJoinColumn(name = "plain_sinister_type_id", referencedColumnName = "id")
    private Map<PlainSinisterType, Coverage> coveragesBySinisterType;

    public Insurance(String code, String name) {
        this.code = code;
        this.name = name;
        contracts = new HashSet<>();
        coveragesBySinisterType = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(float defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public double getMinDeductible() {
        return minDeductible;
    }

    public void setMinDeductible(double minDeductible) {
        this.minDeductible = minDeductible;
    }

    public double getMaxDeductible() {
        return maxDeductible;
    }

    public void setMaxDeductible(double maxDeductible) {
        this.maxDeductible = maxDeductible;
    }

    public Set<Contract> getContracts() {
        return new HashSet<Contract>(contracts);
    }

    public boolean addContract(Contract contract){
        return contracts.add(requireNonNull(contract));
    }

    public Map<PlainSinisterType, Coverage> getCoveragesBySinisterType() {
        return new HashMap<>(coveragesBySinisterType);
    }

    public boolean putCoverageBySinisterType(PlainSinisterType sinisterType, Coverage coverage) {
        if(coveragesBySinisterType.containsKey(requireNonNull(sinisterType)))
            return false;
        coveragesBySinisterType.put(sinisterType, coverage);
        sinisterType.putCoverageByInsurance(this, coverage);
        return true;
    }

    @Override
    public final int hashCode() {
        return code.hashCode();
    }

    Insurance() {}
}