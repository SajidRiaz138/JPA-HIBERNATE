package org.hibernate.relations.efficientmanytomany;

import org.hibernate.Session;
import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hibernate.relations.manytomany.ManyToManyRelationJointTableHasOwnColumns.Course;
import static org.hibernate.relations.manytomany.ManyToManyRelationJointTableHasOwnColumns.Student;
import static org.hibernate.relations.manytomany.ManyToManyRelationJointTableHasOwnColumns.Enrollment;

public class ManyToManyRelationJointTableHasOwnColumnsTest extends AbstractTest
{
    @Test
    public void testManyToManyRelationWithExtraColumn()
    {

        Course cs = new Course("Computer Science", "Good Course", 5);
        Course algorithm = new Course("Algorithm", "Good for logic", 6);
        Course dataStructure = new Course("Data Structure", "Good for programming", 8);

        transactionManager.executeTransaction(entityManager ->
        {
            entityManager.persist(cs);
            entityManager.persist(algorithm);
            entityManager.persist(dataStructure);
        }, entities);

        transactionManager.executeTransaction(entityManager ->
        {
            Session session = entityManager.unwrap(Session.class);
            Course computerScience = session.bySimpleNaturalId(Course.class).load(cs.getCourseName());
            Course algo = session.bySimpleNaturalId(Course.class).load(algorithm.getCourseName());
            Course ds = session.bySimpleNaturalId(Course.class).load(dataStructure.getCourseName());

            Student joe = new Student("joe@yahoo.com", "Joe burn");
            joe.setId(1L);
            joe.addCourse(computerScience);
            joe.addCourse(algo);
            joe.addCourse(ds);

            entityManager.persist(joe);

            Student student = new Student("jimmy@yahoo.com", "Jimy åkeson");
            student.setId(2L);
            student.addCourse(algo);
            student.addCourse(ds);

            entityManager.persist(student);

        }, getEntities());

        transactionManager.executeTransaction(entityManager ->
        {
            Course computerScience = entityManager.unwrap(Session.class)
                    .bySimpleNaturalId(Course.class)
                    .load("Computer Science");

            Student student = entityManager.createQuery("""
                    select s
                    from Student s
                    join fetch s.enrollments se
                    join fetch se.course
                    where s.id = :studentId
                    """, Student.class)
                    .setParameter("studentId", 1L)
                    .getSingleResult();

            Assertions.assertEquals(3, student.getEnrollments().size());
            student.removeCourse(computerScience);
        }, getEntities());

        transactionManager.executeTransaction(entityManager ->
        {
            Student student = entityManager.find(Student.class, 1L);
            Assertions.assertEquals(2, student.getEnrollments().size());
        }, getEntities());
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Course.class, Student.class, Enrollment.class };
    }
}
