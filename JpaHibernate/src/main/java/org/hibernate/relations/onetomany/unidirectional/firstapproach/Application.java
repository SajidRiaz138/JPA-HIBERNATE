package org.hibernate.relations.onetomany.unidirectional.firstapproach;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.hibernatefactory.HibernateFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application
{
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args)
    {
        Post post = new Post();
        post.setTitle("First post");
        Comment firstComment = new Comment();
        firstComment.setReview("My first review");
        Comment secondComment = new Comment();
        secondComment.setReview("My second review");
        Comment thirdComment = new Comment();
        thirdComment.setReview("My Third review");

        post.getComments().add(firstComment);
        post.getComments().add(secondComment);
        post.getComments().add(thirdComment);

        Transaction transaction = null;

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            // start a transaction
            transaction = session.beginTransaction();
            // Add to Context
            session.persist(post);
            // commit transaction
            transaction.commit();
        }
        catch (Exception e)
        {
            LOGGER.log(Level.INFO, "Operation failed : ", e.getCause());
            if (transaction != null)
            {
                transaction.rollback();
                LOGGER.log(Level.INFO, "Transaction rollback successfully! " + transaction.getStatus());
            }

        }

        /*        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            List < Student > students = session.createQuery("from Student", Student.class).list();
            students.forEach(s -> System.out.println(s.getFirstName()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
            }*/
    }
}
