package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "attribute_option")
@Data
@NoArgsConstructor
public class AttributeOption
{
    @Id
    @Column (name = "attribute_option_id")
    private Integer id;

    @Column (name = "attribute_option_name", length = 100)
    private String attributeOptionName;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "attribute_type_id")
    private AttributeType attributeType;

    @ManyToMany (mappedBy = "attributeOptions")
    private final Set<Product> products = new HashSet<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AttributeOption)) return false;
        return id != null && id.equals(((AttributeOption) o).getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
