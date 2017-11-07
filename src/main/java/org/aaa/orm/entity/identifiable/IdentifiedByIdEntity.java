package org.aaa.orm.entity.identifiable;

import org.aaa.orm.entity.UpdatableEntity;
import org.aaa.util.ObjectUtils;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.stream.Stream;


@MappedSuperclass
public abstract class IdentifiedByIdEntity extends UpdatableEntity implements IdentifiableById {

	private static final long serialVersionUID = -5075953598452663724L;

	private static IdGenerator idGenerator;

	static void setIdGenerator(IdGenerator idGenerator) {
		IdentifiedByIdEntity.idGenerator = idGenerator;
	}

	@Id
	//@Access(AccessType.PROPERTY) //allows calling getId without loading whole proxy (use final instead ?)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	{
		idGenerator.generateFor(this);
	}

	@Override
	public final int hashCode() {
		return Long.hashCode(getId());
	}

	@Override
	public final long getId() {
		ObjectUtils.doIf(() -> idGenerator.generateFor(this), id == NULL_ID);
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	@Override
	public final boolean equals(Object that) {
		return that != null &&
				(this instanceof HibernateProxy ?
				this.getClass().getSuperclass() :
				this.getClass())
						==
						(that instanceof HibernateProxy ?
								that.getClass().getSuperclass() :
								that.getClass()) &&
				this.hashCode() == that.hashCode();
	}
}