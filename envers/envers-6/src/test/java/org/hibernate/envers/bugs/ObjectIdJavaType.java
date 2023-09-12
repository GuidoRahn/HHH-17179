package org.hibernate.envers.bugs;

import java.util.Optional;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.java.StringJavaType;

public class ObjectIdJavaType extends AbstractClassJavaType<ObjectId> {
    public static final ObjectIdJavaType INSTANCE = new ObjectIdJavaType();

    public ObjectIdJavaType() {
        super(ObjectId.class);
    }

    public String toString(ObjectId value) {
        return value.toString();
    }

    public ObjectId fromString(CharSequence string) {
        return ObjectId.fromString(string.toString());
    }

    public <X> X unwrap(ObjectId value, Class<X> type, WrapperOptions options) {
        return Optional.ofNullable(value)
                .filter(it -> !type.isInstance(it))
                .map(it -> StringJavaType.INSTANCE.unwrap(it.getId(), type, options))
                .orElseGet(() -> (X) value);
    }

    public <X> ObjectId wrap(X value, WrapperOptions options) {
        return Optional.ofNullable(value)
                .flatMap(o -> cast(o, String.class))
                .map(ObjectId::fromString)
                .orElseGet(() -> (ObjectId) value);
    }

    public static <T> Optional<T> cast(final Object object, final Class<T> destClass) {
        return Optional.ofNullable(object).filter(destClass::isInstance).map(destClass::cast);
    }
}
