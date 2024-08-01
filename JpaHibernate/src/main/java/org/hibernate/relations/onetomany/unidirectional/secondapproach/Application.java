package org.hibernate.relations.onetomany.unidirectional.secondapproach;

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

        Author author = new Author();
        author.setName("John Doe");

        Book book1 = new Book();
        book1.setTitle("Hibernate Basic");
        Book book2 = new Book();
        book2.setTitle("JPA Advanced");

        author.addBook(book1);
        author.addBook(book2);

        Transaction transaction = null;

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            // start a transaction
            transaction = session.beginTransaction();
            // Add to Context
            session.persist(author);
            // commit transaction
            transaction.commit();
        }
        catch (Exception exception)
        {
            handleException(transaction, exception);
        }

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            List<Book> books = session.createQuery("from Book", Book.class).list();
            books.forEach(book -> System.out.println(book.getTitle()));
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
