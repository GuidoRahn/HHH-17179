package org.hibernate.envers.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Embeddable
@Audited
public class MesswertEmbeddable {

	@Column(precision = 24, scale = 12)
	private BigDecimal messwert;

	public void setMesswert(BigDecimal messwert) {
		this.messwert = messwert;
	}
}
