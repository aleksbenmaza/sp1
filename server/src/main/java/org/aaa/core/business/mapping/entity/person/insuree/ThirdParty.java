package org.aaa.core.business.mapping.entity.person.insuree;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "third_parties")
public class ThirdParty extends Insuree {

	private static final long serialVersionUID = 6968875177402393854L;

	public ThirdParty(String nirNumber) {
		super(nirNumber);
	}

	ThirdParty() {}
}
