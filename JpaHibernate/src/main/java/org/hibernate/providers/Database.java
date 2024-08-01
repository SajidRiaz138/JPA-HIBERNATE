package org.hibernate.providers;

import org.hibernate.dialect.Dialect;
import org.hibernate.helpers.ReflectionHelper;

public enum Database
{
    MYSQL
    {
        @Override
        public Class<? extends DataSourceProvider> dataSourceProviderClass()
        {
            return MySQLDataSourceProvider.class;
        }
    };

    public DataSourceProvider dataSourceProvider()
    {
        return ReflectionHelper.newInstance(dataSourceProviderClass().getName());
    }

    public abstract Class<? extends DataSourceProvider> dataSourceProviderClass();

    protected boolean supportsDatabaseName()
    {
        return true;
    }

    protected String databaseName()
    {
        return "db";
    }

    protected boolean supportsCredentials()
    {
        return true;
    }

    public static Database of(Dialect dialect)
    {
        Class<? extends Dialect> dialectClass = dialect.getClass();
        for (Database database : values())
        {
            if (database.dataSourceProvider().hibernateDialectClass().isAssignableFrom(dialectClass))
            {
                return database;
            }
        }
        throw new UnsupportedOperationException(
                String.format(
                        "The provided Dialect [%s] is not supported!",
                        dialectClass));
    }
}
