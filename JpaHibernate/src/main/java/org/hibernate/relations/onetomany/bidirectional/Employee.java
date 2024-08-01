package org.hibernate.relations.onetomany.bidirectional;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing an employee.
 * <p>
 * The Employee entity overrides the equals and hashCode methods. Since natural identifiers cannot be 
 * used for equality checks, the entity identifier is used in the equals method. This approach ensures 
 * consistency across all entity state transitions. The hashCode method is set to a constant value 
 * to maintain this consistency.
 * </p>
 * <p>
 * Overriding equals and hashCode in the child entity is a good practice for bidirectional associations, 
 * as it ensures proper behavior for operations like removeEmployee.
 * </p>
 */

@Entity (name = "Employee")
@Table (name = "employee")
@EqualsAndHashCode
@Setter
@Getter
public class Employee
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne (fetch = FetchType.LAZY)
    private Department department;
}
