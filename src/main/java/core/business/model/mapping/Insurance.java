package app.core.business.model.mapping;

import app.core.business.model.mapping.sinister.PlainSinister;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 02/03/2017.
 */
@Entity
@Table(name = "types_garantie")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Insurance extends IdentifiableByIdImpl {

    @XmlAttribute
    @Column(unique = true)
    private String code;

    @Column(name = "libelle", unique = true)
    private String name;

    @Column(name = "montant_defaut")
    private float defaultAmount;

    @Column(name = "franchise_min")
    private double minDeductible;

    @Column(name = "franchise_max")
    private double maxDeductible;

    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL)
    private Set<Contract> contracts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name               = "types_sinistres_types_garantie",
               inverseJoinColumns = {@JoinColumn(name                 = "type_sinistre_id",
                                                 referencedColumnName = "id")},
               joinColumns        = {@JoinColumn(name                 = "type_garantie_id",
                                                 referencedColumnName = "id")})
    private Set<PlainSinister.Type> sinisterTypes;

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
        return contracts.add(contract);
    }

    public Set<PlainSinister.Type> getSinisterTypes() {
        return new HashSet<PlainSinister.Type>(sinisterTypes);
    }

    public boolean addSinisterType(PlainSinister.Type sinisterType) {
        if(sinisterTypes.contains(sinisterType))
            return false;
        sinisterTypes.add(sinisterType);
        sinisterType.addInsurance(this);
        return true;
    }
}
