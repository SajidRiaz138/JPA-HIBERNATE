package org.hibernate.compositekeymapping;

import static org.hibernate.relations.compositekeymapping.ManyToOneRelationCompositeKey.Department;
import static org.hibernate.relations.compositekeymapping.ManyToOneRelationCompositeKey.Employee;
import static org.hibernate.relations.compositekeymapping.ManyToOneRelationCompositeKey.DepartmentId;

import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManyToOneRelationCompositeKeyTest extends AbstractTest
{
    @Test
    public void testManyToOneRelation()
    {
        transactionManager.executeTransaction(entityManager ->
        {
            // Create and persist Department
            DepartmentId deptId = new DepartmentId(1L, 101L);
            Department department = new Department();
            department.setId(deptId);
            department.setDepartmentName("Human Resources");
            entityManager.persist(department);

        }, entities);

        transactionManager.executeTransaction(entityManager ->
        {
            DepartmentId deptId = new DepartmentId(1L, 101L);
            Department foundDept = entityManager.find(Department.class, deptId);
            Assertions.assertNotNull(foundDept);

            // Create and persist Employees
            Employee emp1 = new Employee();
            emp1.setName("John");
            emp1.setEmployeeId(1L);
            emp1.setDepartment(foundDept);
            Employee emp2 = new Employee();
            emp2.setName("Jane");
            emp2.setEmployeeId(2L);
            emp2.setDepartment(foundDept);

            entityManager.persist(emp1);
            entityManager.persist(emp2);
        }, entities);
        transactionManager.executeTransaction(entityManager ->
        {
            DepartmentId deptId = new DepartmentId(1L, 101L);
            Employee employee = entityManager.find(Employee.class, 1L);
            Assertions.assertNotNull(employee);
            Assertions.assertEquals(deptId, employee.getDepartment().getId());
        }, entities);
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Department.class, Employee.class };
    }
}
