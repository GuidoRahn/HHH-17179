package org.hibernate.envers.bugs;

import jakarta.persistence.*;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.envers.Audited;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:S2160")
@Entity
@Audited
@Table(name = "messwertsammlung")
public class MesswertSammlungEntity {


	public void setObjectId(String objectId) {
		this.objectId = ObjectId.fromString(objectId);
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	@Id
	@JavaType(ObjectIdJavaType.class)
	@JdbcType(VarcharJdbcType.class)
	@Column(length = 36)
	ObjectId objectId;

	@ElementCollection
	@MapKeyColumn(name = "typ")
	@CollectionTable(name = "messwertsammlung_messwerte",
					 foreignKey = @ForeignKey(name = "fk_messwertsammlung"),
					 joinColumns = { @JoinColumn(name = "messwertsammlung_objectid") })
	private Map<Integer, MesswertEmbeddable> messwerte = new HashMap<>();

	public Map<Integer, MesswertEmbeddable> getMesswerte() {
		return messwerte;
	}

	public void setMesswerte(Map<Integer, MesswertEmbeddable> messwerte) {
		this.messwerte = messwerte;
	}
}