package app.core.business.model.mapping;

import app.core.business.model.mapping.person.Expert;
import app.core.business.model.mapping.sinister.Sinister;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import static java.util.Objects.requireNonNull;

/**
 * Created by alexandremasanes on 03/04/2017.
 */
@Entity
@Table(name = "sinistres_contrats_experts")
public class Coverage implements Serializable {

    @Embeddable
    public static class Id implements Serializable {

        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "sinistre_id", referencedColumnName = "id")
        private Sinister sinister;

        public Id(Sinister sinister) {
            this.sinister = requireNonNull(sinister);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id = (Id) o;

            return sinister != null ? sinister.equals(id.sinister) : id.sinister == null;
        }

        @Override
        public int hashCode() {
            return sinister != null ? sinister.hashCode() : 0;
        }

        Id() {

        }
    }

    @EmbeddedId
    Id id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "contrat_id", referencedColumnName = "id")
    private Contract contract;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "expert_id", referencedColumnName = "id")
    private Expert expert;

    public Coverage(Sinister sinister, Contract contract) {
        if(contract.getVehicle() != sinister.getVehicle() || contract.getVehicle().getId() != sinister.getVehicle().getId())
            throw new app.core.business.model.mapping.Entity.BusinessException();
        this.id = new Id(sinister);
        this.contract = requireNonNull(contract);
        sinister.setCoverage(this);
        contract.addCoverage(this);
    }

    public Coverage(Sinister sinister, Contract contract, Expert expert) {
        this(sinister, contract);
        this.expert = requireNonNull(expert);
        expert.addCoverage(this);
    }

    public Sinister getSinister() {
        return id.sinister;
    }

    public Contract getContract() {
        return contract;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        if(expert != null) {
            expert.addCoverage(this);

        } else if(this.expert != null)
            this.expert.removeCoverage(this);

        this.expert = expert;
    }

    @Override
    public boolean equals(Object o) {
        return id.equals(o);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    Coverage() {

    }
}
