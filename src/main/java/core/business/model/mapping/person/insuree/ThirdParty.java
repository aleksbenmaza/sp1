package app.core.business.model.mapping.person.insuree;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tiers")
public class ThirdParty extends Insuree {

	public static final long serialVersionUID = 6968875177402393854L;

}
