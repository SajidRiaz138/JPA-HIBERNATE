package org.hibernate.providers;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.hibernate.dialect.MySQLDialect;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Connection;

public class MySQLDataSourceProvider implements DataSourceProvider
{
    private Boolean rewriteBatchedStatements;

    private Boolean cachePreparedStatements;

    private Boolean useServerPreparedStatements;

    private Boolean useTimezone;

    private Boolean useJDBCCompliantTimezoneShift;

    private Boolean useLegacyDatetimeCode;

    private Boolean useCursorFetch;

    private Integer prepStmtCacheSqlLimit;

    @Override
    public DataSource dataSource()
    {
        DataSource dataSource = newDataSource();
        try (Connection connection = dataSource.getConnection())
        {
            return dataSource;
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    @Override
    public String url()
    {
        return defaultJdbcUrl();
    }

    public boolean isRewriteBatchedStatements()
    {
        return rewriteBatchedStatements;
    }

    public MySQLDataSourceProvider setRewriteBatchedStatements(boolean rewriteBatchedStatements)
    {
        this.rewriteBatchedStatements = rewriteBatchedStatements;
        return this;
    }

    public boolean getCachePreparedStatements()
    {
        return cachePreparedStatements;
    }

    public MySQLDataSourceProvider setCachePreparedStatements(boolean cachePreparedStatements)
    {
        this.cachePreparedStatements = cachePreparedStatements;
        return this;
    }

    public boolean getUseServerPreparedStatements()
    {
        return useServerPreparedStatements;
    }

    public MySQLDataSourceProvider setUseServerPreparedStatements(boolean useServerPreparedStatements)
    {
        this.useServerPreparedStatements = useServerPreparedStatements;
        return this;
    }

    public boolean isUseTimezone()
    {
        return useTimezone;
    }

    public MySQLDataSourceProvider setUseTimezone(boolean useTimezone)
    {
        this.useTimezone = useTimezone;
        return this;
    }

    public boolean isUseJDBCCompliantTimezoneShift()
    {
        return useJDBCCompliantTimezoneShift;
    }

    public MySQLDataSourceProvider setUseJDBCCompliantTimezoneShift(boolean useJDBCCompliantTimezoneShift)
    {
        this.useJDBCCompliantTimezoneShift = useJDBCCompliantTimezoneShift;
        return this;
    }

    public boolean isUseLegacyDatetimeCode()
    {
        return useLegacyDatetimeCode;
    }

    public MySQLDataSourceProvider setUseLegacyDatetimeCode(boolean useLegacyDatetimeCode)
    {
        this.useLegacyDatetimeCode = useLegacyDatetimeCode;
        return this;
    }

    public boolean isUseCursorFetch()
    {
        return useCursorFetch;
    }

    public MySQLDataSourceProvider setUseCursorFetch(boolean useCursorFetch)
    {
        this.useCursorFetch = useCursorFetch;
        return this;
    }

    public Integer getPrepStmtCacheSqlLimit()
    {
        return prepStmtCacheSqlLimit;
    }

    public MySQLDataSourceProvider setPrepStmtCacheSqlLimit(Integer prepStmtCacheSqlLimit)
    {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
        return this;
    }

    @Override
    public String hibernateDialect()
    {
        return MySQLDialect.class.getName();
    }

    @Override
    public String defaultJdbcUrl()
    {
        return "jdbc:mysql://localhost:3306/db?useSSL=false";
    }

    @Override
    public DataSource newDataSource()
    {
        try
        {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(url());
            dataSource.setUser(username());
            dataSource.setPassword(password());

            if (rewriteBatchedStatements != null)
            {
                dataSource.setRewriteBatchedStatements(rewriteBatchedStatements);
            }
            if (useCursorFetch != null)
            {
                dataSource.setUseCursorFetch(useCursorFetch);
            }
            if (cachePreparedStatements != null)
            {
                dataSource.setCachePrepStmts(cachePreparedStatements);
            }
            if (useServerPreparedStatements != null)
            {
                dataSource.setUseServerPrepStmts(useServerPreparedStatements);
            }
            if (prepStmtCacheSqlLimit != null)
            {
                dataSource.setPrepStmtCacheSqlLimit(prepStmtCacheSqlLimit);
            }

            return dataSource;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("The DataSource could not be instantiated!");
        }
    }

    @Override
    public Class<? extends DataSource> dataSourceClassName()
    {
        return MysqlDataSource.class;
    }

    @Override
    public Properties dataSourceProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("url", url());
        return properties;
    }

    @Override
    public String username()
    {
        return "root";
    }

    @Override
    public String password()
    {
        return "root";
    }

    @Override
    public Database database()
    {
        return Database.MYSQL;
    }

    @Override
    public String toString()
    {
        return "MySQLDataSourceProvider{" +
                "cachePrepStmts=" + cachePreparedStatements +
                ", useServerPrepStmts=" + useServerPreparedStatements +
                ", rewriteBatchedStatements=" + rewriteBatchedStatements +
                '}';
    }
}
