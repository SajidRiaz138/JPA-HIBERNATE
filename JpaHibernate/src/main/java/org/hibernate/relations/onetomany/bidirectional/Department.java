package org.hibernate.relations.onetomany.bidirectional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a department.
 * <p>
 * This class demonstrates a bidirectional `@OneToMany` relationship with the `Employee` entity.
 * the bidirectional @OneToMany association is the best way to map a one-to-many database relationship
 * when we really need the collection on the parent side of the association.
 * </p>
 * <p>
 * The bidirectional `@OneToMany` association is the best way to map a one-to-many database relationship
 * when the collection is needed on the parent side of the association. The `@ManyToOne` side is used to 
 * propagate all entity state changes. The `@ManyToOne` association uses `FetchType.LAZY` to avoid 
 * performance issues associated with `EAGER` fetching.
 * </p>
 * <p>
 * This entity features utility methods (e.g., `addEmployee` and `removeEmployee`) to synchronize both sides 
 * of the bidirectional association. These methods should always be provided to prevent subtle state 
 * propagation issues.
 * </p>
 */

@Entity (name = "Department")
@Table (name = "department")
@Setter
@Getter
public class Department
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany (mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee)
    {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee)
    {
        employees.remove(employee);
        employee.setDepartment(null);
    }
}
