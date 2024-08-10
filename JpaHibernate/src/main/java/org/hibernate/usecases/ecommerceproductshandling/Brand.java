package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "brand")
@Data
@NoArgsConstructor
public class Brand
{
    @Id
    @Column (name = "brand_id")
    private Integer id;
    @Column (name = "brand_name", length = 200)
    private String brandName;
    @Column (name = "brand_description", length = 2000)
    private String brandDescription;

    @OneToMany (mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Product> products = new HashSet<>();

    public void addProduct(Product product)
    {
        products.add(product);
        product.setBrand(this);
    }

    public void removeProduct(Product product)
    {
        products.add(product);
        product.setBrand(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        return id != null && id.equals(((Brand) o).getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
