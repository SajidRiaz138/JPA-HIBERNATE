package org.hibernate.relations.efficientonetoone;

import static org.hibernate.relations.efficientonetoone.OneToOneMappingNoNplusQuery.Department;
import static org.hibernate.relations.efficientonetoone.OneToOneMappingNoNplusQuery.DepartmentDetails;
import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OneToOneMappingNoNplusQueryTest extends AbstractTest
{

    @Test
    public void testNoNPlusOne()
    {
        statistics.clear();
        transactionManager.executeTransaction(entityManager ->
        {
            for (int i = 1; i <= 10; i++)
            {
                Department post = new Department();
                post.setName(String.format("Department nr. %d", i));
                DepartmentDetails details = new DepartmentDetails("John from Cave!");
                post.setDetails(details);
                entityManager.persist(post);
            }
        }, entities);

        List<Department> posts = transactionManager.executeTransaction(entityManager ->
        {
            return entityManager.createQuery("""
                    select d
                    from Department d
                    where d.name like 'Department nr.%'
                    """, Department.class).getResultList();
        }, entities);
        Assertions.assertEquals(10, posts.size());
        Assertions.assertEquals(1, statistics.getQueryExecutionCount());
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Department.class, DepartmentDetails.class };
    }
}
