package org.hibernate.transactionmanagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import org.hibernate.hibernatefactory.JpaEntityManagerFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class TransactionManager
{
    private static final Logger LOGGER = Logger.getLogger(TransactionManager.class.getName());

    private final EntityManagerFactory entityManagerFactory;

    public TransactionManager(final Class<?>[] entities)
    {
        entityManagerFactory = JpaEntityManagerFactory.getEntityManagerFactory(entities);
    }

    public  <T> T executeTransaction(final Function<EntityManager, T> function, final Class<?>[] entities)
    {
        T result = null;
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try
        {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            result = function.apply(entityManager);
            if (!transaction.getRollbackOnly())
            {
                transaction.commit();
            }
            else
            {
                try
                {
                    transaction.rollback();
                }
                catch (Exception exception)
                {
                    LOGGER.log(Level.SEVERE, "Rollback failed", exception);
                }
            }
        }
        catch (Throwable t)
        {
            if (transaction != null && transaction.isActive())
            {
                try
                {
                    transaction.rollback();
                }
                catch (Exception exception)
                {
                    LOGGER.log(Level.SEVERE, "Rollback failed", exception);
                }
            }
            throw t;
        }
        finally
        {
            JpaEntityManagerFactory.closeEntityManager(entityManager);
        }
        return result;
    }

    public void close()
    {
        if (entityManagerFactory != null && entityManagerFactory.isOpen())
        {
            entityManagerFactory.close();
        }
        JpaEntityManagerFactory.closeDatasource();
    }

    public void executeTransaction(Consumer<EntityManager> function, Class<?>[] entities)
    {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try
        {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            function.accept(entityManager);
            if (!transaction.getRollbackOnly())
            {
                transaction.commit();
            }
            else
            {
                try
                {
                    transaction.rollback();
                }
                catch (Exception exception)
                {
                    LOGGER.log(Level.SEVERE, "Rollback failed", exception);
                }
            }
        }
        catch (Throwable t)
        {
            if (transaction != null && transaction.isActive())
            {
                try
                {
                    transaction.rollback();
                }
                catch (Exception exception)
                {
                    LOGGER.log(Level.SEVERE, "Rollback failed", exception);
                }
            }
            throw t;
        }
        finally
        {
            JpaEntityManagerFactory.closeEntityManager(entityManager);
        }
    }
}
