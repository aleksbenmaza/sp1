package org.aaa.orm.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 23/09/2017.
 */

@Immutable
@MappedSuperclass
public abstract class ImmutableEntity extends BaseEntity {}
