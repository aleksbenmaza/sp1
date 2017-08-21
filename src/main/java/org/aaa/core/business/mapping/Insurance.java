package org.aaa.core.business.mapping;

import static org.aaa.core.business.mapping.sinister.PlainSinister.Type;
import org.aaa.core.business.mapping.sinister.PlainSinister;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 02/03/2017.
 */
@Entity
@Table(name = "insurances")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Insurance extends IdentifiableByIdImpl {

    @Column(
            unique = true
    ) private String code;

    @Column(
            name   = "name",
            unique = true
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

    @ManyToMany(
            cascade  = CascadeType.ALL,
            mappedBy = "insurances"
    ) private Set<PlainSinister.Type> sinisterTypes;

    public Insurance() {
        contracts = new HashSet<Contract>();
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

    public Set<PlainSinister.Type> getSinisterTypes() {
        return new HashSet<PlainSinister.Type>(sinisterTypes);
    }

    public boolean addSinisterType(Type sinisterType) {
        if(sinisterTypes.contains(requireNonNull(sinisterType)))
            return false;
        sinisterTypes.add(sinisterType);
        sinisterType.addInsurance(this);
        return true;
    }
}