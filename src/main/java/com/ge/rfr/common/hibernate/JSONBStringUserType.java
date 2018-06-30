package com.ge.rfr.common.hibernate;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.StringTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.postgresql.util.PGobject;

import java.sql.*;

public class JSONBStringUserType extends AbstractSingleColumnStandardBasicType<String> {

    public JSONBStringUserType() {
        super(new TypeDescriptor(), StringTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "jsonb-string";
    }

    public static class TypeDescriptor implements SqlTypeDescriptor {

        @Override
        public int getSqlType() {
            return Types.OTHER;
        }

        @Override
        public boolean canBeRemapped() {
            return true;
        }

        @Override
        public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                    PGobject dbObject = new PGobject();
                    dbObject.setType("jsonb");
                    dbObject.setValue(javaTypeDescriptor.unwrap(value, String.class, options));
                    st.setObject(index, dbObject, getSqlType());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options) throws SQLException {
                    PGobject dbObject = new PGobject();
                    dbObject.setType("jsonb");
                    dbObject.setValue(javaTypeDescriptor.unwrap(value, String.class, options));
                    st.setObject(name, dbObject, getSqlType());
                }
            };
        }

        @Override
        public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicExtractor<X>(javaTypeDescriptor, this) {
                @Override
                protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(rs.getString(name), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getString(index), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getString(name), options);
                }
            };
        }

    }

}
