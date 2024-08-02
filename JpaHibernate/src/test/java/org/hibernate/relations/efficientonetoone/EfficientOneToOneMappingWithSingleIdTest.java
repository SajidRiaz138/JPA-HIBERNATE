package org.hibernate.relations.efficientonetoone;

import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hibernate.relations.efficientonetoone.EfficientOneToOneMappingWithSingleId.Department;
import static org.hibernate.relations.efficientonetoone.EfficientOneToOneMappingWithSingleId.DepartmentDetails;

public class EfficientOneToOneMappingWithSingleIdTest extends AbstractTest
{
    @Test
    public void testMapsIdOneToOneMapping()
    {
        transactionManager.executeTransaction(entityManager ->
        {
            final Department hrDepartment = new Department();
            hrDepartment.setName("HR");
            entityManager.persist(hrDepartment);
        }, getEntities());

        transactionManager.executeTransaction(entityManager ->
        {
            Department department = entityManager.find(Department.class, 1L);
            DepartmentDetails details = new DepartmentDetails("HR Department");
            details.setDepartment(department);
            entityManager.persist(details);
        }, getEntities());

        transactionManager.executeTransaction(entityManager ->
        {
            Department department = entityManager.find(Department.class, 1L);
            DepartmentDetails details = entityManager.find(DepartmentDetails.class, department.getId());
            Assertions.assertNotNull(details);
            entityManager.flush();
        }, getEntities());
    }

    public Class<?>[] getEntities()
    {
        return new Class[] { Department.class, DepartmentDetails.class };
    }
}
