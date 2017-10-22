package org.aaa.core.business.mapping.sinister;


import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.ToBeChecked;
import org.aaa.core.business.mapping.damage.Damage;
import org.aaa.core.business.mapping.person.Expert;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.Date;
import java.sql.Time;



/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Table(name = "sinisters")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sinister extends IdentifiedByIdEntity implements ToBeChecked {

    @ManyToOne(
            cascade = CascadeType.ALL
    ) @JoinColumn(
            name                 = "contract_id",
            referencedColumnName = "id",
            nullable = false
    ) protected Contract contract;

    @ManyToOne(
            cascade = CascadeType.ALL
    ) @JoinColumn(
            name                 = "expert_id",
            referencedColumnName = "id"
    ) protected Expert expert;

    @OneToOne(
            mappedBy = "id.sinister",
            cascade  = CascadeType.ALL,
            fetch    = FetchType.LAZY
    ) protected Damage damage;

    @Column(
    ) protected Date date;

    @Column(
    ) protected Time time;

    @Column(
    ) protected String comment;

    @Enumerated
    @Column(
    ) private Status status;

    @Column(
            name = "is_closed"
    ) protected boolean closed;

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Sinister(Contract contract) {
        this.contract = IdentifiedByIdEntity.requireNonNull(contract);
        contract.addSinister(this);
    }

    public Sinister(Contract contract, Expert expert) {
        this(contract);
        this.expert = requireNonNull(expert);
        expert.addSinister(this);
    }

    public Contract getContract() {
        return contract;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public Expert getExpert() {
        return expert;
    }

    protected Sinister() {}
}