package org.aaa.orm.entity.identifiable;

import org.aaa.orm.entity.UpdatableEntity;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(IdentifiableByEntityListener.class)
public abstract class IdentifiedByIdEntity extends UpdatableEntity implements IdentifiableById {

	private static final long serialVersionUID = -5075953598452663724L;

	@Id
	@Access(AccessType.PROPERTY) //allows calling getId without loading whole proxy (use final instead ?)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Override
	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object that) {
		if(that == null)
			return false;
		if(that == this)
			return true;
		if(that instanceof IdentifiedByIdEntity && resolvedClass(this) == resolvedClass((IdentifiedByIdEntity)that))
			if(((IdentifiedByIdEntity)that).id + this.id == NULL_ID * 2)
				return that.hashCode() == this.hashCode();
			else
				return ((IdentifiedByIdEntity) that).id == this.id;
		else
			return false;
	}
}