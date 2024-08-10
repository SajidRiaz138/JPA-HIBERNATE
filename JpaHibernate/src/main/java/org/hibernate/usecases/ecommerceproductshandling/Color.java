package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "color")
@Data
@NoArgsConstructor
public class Color
{
    @Id
    @Column (name = "color_id")
    private Integer id;

    @Column(name = "color_name", length = 100)
    private String colorName;

    @OneToMany (mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProductItem> productItems = new HashSet<>();

    public void addProductItem(ProductItem productItem)
    {
        productItems.add(productItem);
        productItem.setColor(this);
    }

    public void removeProductItem(ProductItem productItem)
    {
        productItems.remove(productItem);
        productItem.setColor(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        return id != null && id.equals(((Color) o).getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
