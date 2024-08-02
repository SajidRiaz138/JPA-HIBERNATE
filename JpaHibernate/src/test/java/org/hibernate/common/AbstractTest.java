package org.hibernate.common;

import org.hibernate.SessionFactory;
import org.hibernate.relations.efficientonetoone.OneToOneMappingNoNplusQuery;
import org.hibernate.stat.Statistics;
import org.hibernate.transactionmanagement.TransactionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTest
{
    protected static TransactionManager transactionManager;
    protected static Class<?>[] entities;
    protected static Statistics statistics;

    @BeforeEach
    public void setUp()
    {
        if (transactionManager == null || entities == null)
        {
            entities = getEntities();
            transactionManager = new TransactionManager(entities);
            statistics = transactionManager.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
            statistics.setStatisticsEnabled(true);
        }
    }

    @AfterAll
    public static void tearDown()
    {
        if (transactionManager != null)
        {
            transactionManager.close();
        }
    }

    public abstract Class<?>[] getEntities();
}
