package org.hibernate.envers.bugs;

import java.io.Serializable;

public final class ObjectId implements Serializable, Comparable<ObjectId> {

	private static final long serialVersionUID = 1L;

	private static final String CLASSID_FORMAT = "%04x";

	private final String id;

	private ObjectId() {
		this(null);
	}

	private ObjectId(final String id) {
		this.id = id;
	}

	ObjectId(final int classId, final String uuid) {
		this(String.format(CLASSID_FORMAT, classId) + uuid);
	}

	public static ObjectId generate(final int classId) {
		return new ObjectId(classId, "RandomGuid.generate()");
	}

	public static ObjectId fromString(final String id) {
		return new ObjectId(id);
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final ObjectId objectId = (ObjectId) o;

		return id != null ? id.equals(objectId.id) : objectId.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public int compareTo(final ObjectId o) {
		return id.compareTo(o.id);
	}

	@Override
	public String toString() {
		return id;
	}

	public boolean isNull() {
		return false;
	}
}
