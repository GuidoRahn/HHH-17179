

package org.hibernate.envers.bugs;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.*;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.hibernate.usertype.EnhancedUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Optional;

public class ObjectIdUserType extends AbstractClassJavaType<ObjectId> implements EnhancedUserType<ObjectId>, JavaType<ObjectId> {

    public static final int OBJECT_ID_SQL_TYPE = Integer.MAX_VALUE;

    public ObjectIdUserType() {
        super(ObjectId.class);
    }

    // UserType Methods
    @Override
    public int getSqlType() {
        return OBJECT_ID_SQL_TYPE;
    }

    @Override
    public Class<ObjectId> returnedClass() {
        return ObjectId.class;
    }

    @Override
    public boolean equals(final ObjectId x, final ObjectId y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(final ObjectId x) {
        return Optional.ofNullable(x).map(ObjectId::hashCode).orElse(0);
    }

    @Override
    public ObjectId nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        if (rs != null) {
            return ObjectId.fromString(rs.getString(position));
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, ObjectId value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value != null) {
            st.setString(index, value.getId());
        } else {
            st.setNull(index, Types.VARCHAR);
        }
    }

    @Override
    public ObjectId deepCopy(final ObjectId value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(final ObjectId value) {
        return value;
    }

    @Override
    public ObjectId assemble(final Serializable cached, final Object owner) {
        return (ObjectId) cached;
    }

    // EnhancedUserType Methods
    @Override
    public String toSqlLiteral(final ObjectId value) {
        return toString(value);
    }

    @Override
    public String toString(final ObjectId value) throws HibernateException {
        return Optional.ofNullable(value).map(ObjectId::getId).orElse(null);
    }

    @Override
    public ObjectId fromStringValue(final CharSequence sequence) throws HibernateException {
        return ObjectId.fromString(sequence.toString());
    }

    // JavaType Methods

    @Override
    public long getDefaultSqlLength(Dialect dialect, JdbcType jdbcType) {
        return 36;
    }

    @Override
    public int getDefaultSqlPrecision(Dialect dialect, JdbcType jdbcType) {
        return EnhancedUserType.super.getDefaultSqlPrecision(dialect, jdbcType);
    }

    @Override
    public int getDefaultSqlScale(Dialect dialect, JdbcType jdbcType) {
        return EnhancedUserType.super.getDefaultSqlScale(dialect, jdbcType);
    }

    @Override
    public Class<ObjectId> getJavaTypeClass() {
        return returnedClass();
    }

    @Override
    public MutabilityPlan<ObjectId> getMutabilityPlan() {
        return ImmutableMutabilityPlan.instance();
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators context) {
        return context.getJdbcType(getSqlType());
    }

    @Override
    public boolean areEqual(ObjectId one, ObjectId another) {
        return equals(one, another);
    }

    @Override
    public ObjectId fromString(CharSequence string) {
        return fromStringValue(string);
    }

    @Override
    public <X> X unwrap(ObjectId value, Class<X> type, WrapperOptions options) {
        // noinspection unchecked
        return Optional.ofNullable(value)
                .filter(it -> !type.isInstance(it))
                .map(it -> StringJavaType.INSTANCE.unwrap(it.getId(), type, options))
                .orElseGet(() -> (X) value);
    }

    @Override
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
