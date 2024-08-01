package org.hibernate.relations.efficientonetoone;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.hibernatefactory.HibernateFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EfficientOneToOneMappingTwoId
{
    private static final Logger LOGGER = Logger.getLogger(EfficientOneToOneMappingTwoId.class.getName());

    public static void main(String[] args)
    {
        Transaction transaction = null;

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
           Engine engine = new Engine(1L);
           Garage garage = new Garage(1L);

           Car car = new Car(engine, garage);
           car.setCarId(1L);

           // start a transaction
            transaction = session.beginTransaction();
            // Add to Context

            session.persist(car);

            transaction.commit();
        }
        catch (Exception exception)
        {
            handleException(transaction, exception);
        }

        try (Session session = HibernateFactory.getSessionFactory().openSession())
        {
            Car car = session.find(Car.class, 1L);
            LOGGER.log(Level.INFO, "Car Engine Id {0}: ", car.getEngine().getId());
            LOGGER.log(Level.INFO, "Car Garage Id {0}: ", car.getGarage().getId());
        }
        catch (Exception exception)
        {
            handleException(transaction, exception);
        }
    }

    private static void handleException(Transaction transaction, Exception exception)
    {
        LOGGER.log(Level.INFO, "Operation failed : ", exception.getCause());
        if (transaction != null && transaction.isActive())
        {
            transaction.rollback();
            LOGGER.log(Level.INFO, "Transaction rollback successfully! " + transaction.getStatus());
        }
    }
}

@Entity (name = "Engine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Engine
{
    @Id
    private Long id;
}

@Entity (name = "Garage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Garage
{
    @Id
    private Long id;
}

@Entity (name = "Car")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
class Car
{
    @Id
    private Long carId;

    @NonNull
    @OneToOne
    @MapsId
    @JoinColumn (name = "id")
    private Engine engine;

    @NonNull
    @OneToOne
    @MapsId
    @JoinColumn (name = "id")
    private Garage garage;
}
