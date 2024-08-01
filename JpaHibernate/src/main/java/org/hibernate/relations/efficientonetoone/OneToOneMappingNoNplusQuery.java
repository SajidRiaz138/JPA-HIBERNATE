package org.hibernate.relations.efficientonetoone;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

public class OneToOneMappingNoNplusQuery
{
    @Entity(name = "Department")
    @Table(name = "department")
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Department
    {
        @Id
        @GeneratedValue
        private Long id;

        private String name;

                // optional = false, set false to avoid N+1 Query
        @OneToOne (mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
        private DepartmentDetails details;

        public void setDetails(DepartmentDetails details)
        {
            if (details == null)
            {
                if (this.details != null)
                {
                    this.details.setDepartment(null);
                }
            }
            else
            {
                details.setDepartment(this);
            }
            this.details = details;
        }
    }

    @Entity (name = "DepartmentDetails")
    @Table (name = "department_details")
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DepartmentDetails
    {
        @Id
        private Long id; // Do not use any id generation strategy. This would be mapped to Department. so shared primary key

        @Column(name = "created_on")
        private LocalDate createdOn;

        @Column (name = "created_by")
        private String createdBy;

        @OneToOne (fetch = FetchType.LAZY)
        @MapsId
        @JoinColumn (name = "id") // map to parent id column
        // by default, the @MapsId annotation does not use the entity identifier name to match the underlying table Primary Key column
        private Department department;

        public DepartmentDetails(String createdBy)
        {
            createdOn = LocalDate.now();
            this.createdBy = createdBy;
        }
    }
}
