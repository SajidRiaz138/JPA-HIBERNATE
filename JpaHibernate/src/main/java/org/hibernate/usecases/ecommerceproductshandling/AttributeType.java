package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "attribute_type")
@Data
@NoArgsConstructor
public class AttributeType
{
    @Id
    @Column (name = "attribute_type_id")
    private Integer id;

    @Column (name = "attribute_name")
    private String attributeName;

    @OneToMany (mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AttributeOption> attributeOptions = new HashSet<>();

    public void addAttributeOption(AttributeOption attributeOption)
    {
        attributeOptions.add(attributeOption);
        attributeOption.setAttributeType(this);
    }

    public void removeAttributeOption(AttributeOption attributeOption)
    {
        attributeOptions.remove(attributeOption);
        attributeOption.setAttributeType(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AttributeType)) return false;
        return id != null && id.equals(((AttributeType) o).getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
