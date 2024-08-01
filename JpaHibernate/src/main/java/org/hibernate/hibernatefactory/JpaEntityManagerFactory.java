package org.hibernate.hibernatefactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import lombok.Getter;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.persistenceunit.PersistenceUnitInfoImpl;
import org.hibernate.providers.DataSourceProvider;
import org.hibernate.providers.Database;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
public class JpaEntityManagerFactory
{
    private static final Logger LOGGER = Logger.getLogger(JpaEntityManagerFactory.class.getName());
    private final static List<Closeable> closeableList = new ArrayList<>();

    public static EntityManagerFactory getEntityManagerFactory(Class<?>[] entityClasses)
    {
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(JpaEntityManagerFactory.class.getName(), entityClasses);
        return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), propertiesToMap(getProperties()))
                .build();
    }

    protected static PersistenceUnitInfo getPersistenceUnitInfo(String name, Class<?>[] entityClasses)
    {
        return new PersistenceUnitInfoImpl(name, entityClassNames(entityClasses), getProperties());
    }

    private static List<String> entityClassNames(Class<?>[] entityClasses)
    {
        return Arrays.stream(entityClasses).map(Class::getName).collect(Collectors.toList());
    }

    public static void closeEntityManager(final EntityManager entityManager)
    {
        if (entityManager != null)
        {
            entityManager.close();
        }
    }

    public static void closeDatasource()
    {
        for (Closeable closeable : closeableList)
        {
            try
            {
                closeable.close();
            }
            catch (IOException exception)
            {
                LOGGER.log(Level.INFO, "Failure {0}", exception);
            }
        }
        closeableList.clear();
    }

    protected static Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        // properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.generate_statistics", "true");
        properties.put("hibernate.type", "TRACE");
        // properties.put("hibernate.dialect", dataSourceProvider().hibernateDialect());
        DataSource dataSource = newDataSource();
        if (dataSource != null)
        {
            properties.put("hibernate.connection.datasource", dataSource);
        }
        properties.put("hibernate.generate_statistics", Boolean.TRUE.toString());
        return properties;
    }

    private static DataSource newDataSource()
    {
        DataSource dataSource = dataSourceProvider().dataSource();
        if (connectionPooling())
        {
            HikariDataSource poolingDataSource = connectionPoolDataSource(dataSource);
            closeableList.add(poolingDataSource);
            return poolingDataSource;
        }
        else
        {
            return dataSource;
        }
    }

    private static HikariDataSource connectionPoolDataSource(DataSource dataSource)
    {
        return new HikariDataSource(hikariConfig(dataSource));
    }

    private static HikariConfig hikariConfig(DataSource dataSource)
    {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(connectionPoolSize());
        hikariConfig.setDataSource(dataSource);
        return hikariConfig;
    }

    private static int connectionPoolSize()
    {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        return cpuCores * 4;
    }

    private static DataSourceProvider dataSourceProvider()
    {
        return Database.MYSQL.dataSourceProvider();
    }

    private static boolean connectionPooling()
    {
        return true;
    }

    private static Map<String, Object> propertiesToMap(Properties properties)
    {
        Map<String, Object> map = new HashMap<>();
        for (String name : properties.stringPropertyNames())
        {
            map.put(name, properties.get(name));
        }
        return map;
    }
}
