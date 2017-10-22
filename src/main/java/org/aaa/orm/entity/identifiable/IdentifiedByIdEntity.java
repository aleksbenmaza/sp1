package org.aaa.orm.entity.identifiable;

import static org.aaa.util.ObjectUtils.doIf;

import org.aaa.orm.entity.UpdatableEntity;
import org.aaa.util.ObjectUtils;

import javax.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class IdentifiedByIdEntity extends UpdatableEntity implements IdentifiableById {

	private static final long serialVersionUID = -5075953598452663724L;

	private static IdGenerator idGenerator;

	static void setIdGenerator(IdGenerator idGenerator) {
		IdentifiedByIdEntity.idGenerator = idGenerator;
	}

	@Id
	//@Access(AccessType.PROPERTY) //allows calling getId without loading whole proxy (use final instead ?)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Basic(fetch = FetchType.EAGER)
	@Column(name = "java_id", unique = true)
	private UUID uuid;

	@Override
	public int hashCode() {
		//ObjectUtils.doIf(() -> id = idGenerator.generateFor(this), id == NULL_ID);
		generateIfNullUUID();
		return uuid.hashCode();
	}

	@Override
	public long getId() {
		System.err.println(getClass());
		return id;
	}
	/*
	//needed for property access, set to private for security reason
	void setId(long id) {
		this.id = id;
	}
*/
	@Override
	public boolean equals(Object o) {
		return o != null && this.hashCode() == o.hashCode();
	}

	//should test if hibernate triggers lazy loading on loading event
	@PostLoad
	private void generateIfNullUUID() {
		uuid = ObjectUtils.ifNull(uuid, UUID::randomUUID);
	}
}