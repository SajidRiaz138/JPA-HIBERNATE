package org.hibernate.relations.onetomany.bidirectional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.hibernatefactory.HibernateFactory;

public class Application
{
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args)
    {

        Department department = new Department();
        department.setName("HR");

        Employee employee1 = new Employee();
        employee1.setName("John Doe");

        Employee employee2 = new Employee();
        employee2.setName("Jane Dean");

        department.addEmployee(employee1);
        department.addEmployee(employee2);

        Transaction transaction = null;

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            // start a transaction
            transaction = session.beginTransaction();
            // Add to Context
            session.persist(department);
            // commit transaction
            transaction.commit();
        }
        catch (Exception exception)
        {
            handleException(transaction, exception);
        }

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            List<Employee> employees = session.createQuery(
                    "select e " +
                            "from Employee e " +
                            "where e.department.id = :departmentId",
                    Employee.class)
                    .setParameter("departmentId", 1L)
                    .getResultList();
            employees.forEach(employee -> System.out.println(employee.getDepartment().getName()));
        }
        catch (Exception exception)
        {
            handleException(transaction, exception);
        }
    }

    private static void handleException(Transaction transaction, Exception exception)
    {
        LOGGER.log(Level.INFO, "Operation failed : ", exception.getCause());
        if (transaction != null)
        {
            transaction.rollback();
            LOGGER.log(Level.INFO, "Transaction rollback successfully! " + transaction.getStatus());
        }
    }
}
