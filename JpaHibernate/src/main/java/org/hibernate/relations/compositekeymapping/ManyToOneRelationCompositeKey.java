package org.hibernate.relations.compositekeymapping;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * USE CASE: In an organization, each department has multiple employees. The Departments table has a composite primary key
 * consisting of department_id and location_id. This composite key uniquely identifies each department.
 * The Departments table has a one-to-many relationship with the Employees table.
 */
public class ManyToOneRelationCompositeKey
{

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class DepartmentId implements Serializable
    {
        @Column (name = "department_id")
        private Long departmentId;
        @Column (name = "location_id")
        private Long locationId;
    }

    @Entity
    @Table (name = "departments")
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Department
    {
        @EmbeddedId
        private DepartmentId id;

        private String departmentName;
    }

    @Entity
    @Table (name = "employees")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Employee
    {

        @Id
        @Column (name = "employee_id")
        private Long employeeId;
        private String name;

        @ManyToOne
        @JoinColumns ({ @JoinColumn (name = "department_id", referencedColumnName = "department_id"),
                        @JoinColumn (name = "location_id", referencedColumnName = "location_id")
        })
        private Department department;
    }

}
