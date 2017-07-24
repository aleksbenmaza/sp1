package app.core.business.model.mapping;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = -5075953598452663724L;

	/**
	 * Created by alexandremasanes on 03/03/2017.
	 */
	public static class BusinessException extends RuntimeException {
	}

	@Generated(GenerationTime.ALWAYS)
	@Column(name = "maj")
	protected Timestamp updatedAt;
	
	public Timestamp updatedAt() {
		return updatedAt;
	}

	protected Entity() {}

	protected static <T extends Entity> T requireNonNull(T entity) {
		return Objects.requireNonNull(entity);
	}

	protected static <T extends Entity> void requireNonNull(T... entities) {

		for(Entity entity : entities)
			if(entity == null)
				throw new NullPointerException();
	}
}