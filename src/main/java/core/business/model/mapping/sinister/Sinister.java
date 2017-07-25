package core.business.model.mapping.sinister;



import core.business.model.mapping.*;
import core.business.model.mapping.damage.Damage;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.sql.Time;
import java.util.Objects;


/**
 * Created by alexandremasanes on 30/01/2017.
 */
@Entity
@Table(name = "sinistres")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sinister extends IdentifiableByIdImpl implements ToBeChecked {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id", referencedColumnName = "id")
    protected Vehicle vehicle;

    @OneToOne(mappedBy = "id.sinister",
              cascade  = CascadeType.ALL)
    protected Coverage coverage;

    @OneToOne(mappedBy = "id.sinister",
              cascade  = CascadeType.ALL)
    protected Damage damage;

    @Column
    protected Date date;

    @Column(name = "heure")
    protected Time time;

    @Column(name = "commentaire")
    protected String comment;

    @Column(name = "statut")
    @Enumerated
    private Status status;

    @Column(name = "ferme")
    protected boolean closed;

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Sinister(Vehicle vehicle) {
        this.vehicle = requireNonNull(vehicle);
        vehicle.addSinister(this);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = Objects.requireNonNull(coverage);
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

    Sinister() {}
}