package org.hibernate.providers;

import org.hibernate.dialect.Dialect;
import org.hibernate.helpers.ReflectionHelper;

import java.util.Properties;
import javax.sql.DataSource;

public interface DataSourceProvider
{

    enum IdentifierStrategy
    {
        IDENTITY,
        SEQUENCE
    }

    String hibernateDialect();

    DataSource dataSource();

    Class<? extends DataSource> dataSourceClassName();

    Properties dataSourceProperties();

    String url();

    String username();

    String password();

    Database database();

    default Class<? extends Dialect> hibernateDialectClass()
    {
        return ReflectionHelper.getClass(hibernateDialect());
    }

    String defaultJdbcUrl();

    DataSource newDataSource();
}
