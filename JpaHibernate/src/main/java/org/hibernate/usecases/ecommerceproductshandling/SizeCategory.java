package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "size_category")
@Data
@NoArgsConstructor
public class SizeCategory
{
    @Id
    @Column (name = "category_id")
    private Integer categoryId;

    @Column (name = "category_name", length = 100)
    private String categoryName;

    @OneToMany (mappedBy = "sizeCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories = new ArrayList<>();

    @OneToMany (mappedBy = "sizeCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SizeOption> sizeOptions = new ArrayList<>();

    public void addProductCategory(ProductCategory category)
    {
        productCategories.add(category);
        category.setSizeCategory(this);
    }

    public void removeProductCategory(ProductCategory category)
    {
        productCategories.remove(category);
        category.setSizeCategory(null);
    }

    public void addSizeOption(SizeOption sizeOption)
    {
        sizeOptions.add(sizeOption);
        sizeOption.setSizeCategory(this);
    }

    public void removeSizeOption(SizeOption sizeOption)
    {
        sizeOptions.remove(sizeOption);
        sizeOption.setSizeCategory(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof SizeCategory sc)) return false;
        return categoryId!= null && categoryId.equals(sc.getCategoryId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
