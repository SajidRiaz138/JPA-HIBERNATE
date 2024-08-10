package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "product_category")
@Data
@NoArgsConstructor
public class ProductCategory
{
    @Id
    @Column (name = "product_category_id")
    private Integer id;

    @Column (name = "category_name", length = 100)
    private String categoryName;

    @Column (name = "category_image", length = 400)
    private String categoryImage;

    @Column (name = "category_description", length = 2000)
    private String categoryDescription;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_product_category_id")
    private ProductCategory parentCategory;

    @OneToMany (mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCategory> subCategories = new HashSet<>();

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "size_category_id")
    private SizeCategory sizeCategory;

    @OneToMany (mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Product> products = new HashSet<>();

    public void addSubCategory(ProductCategory category)
    {
        this.subCategories.add(category);
        category.setParentCategory(this);
    }

    public void removeCategory(ProductCategory productCategory)
    {
        subCategories.remove(productCategory);
        productCategory.setParentCategory(null);
    }

    public void moveCategory(ProductCategory newParent)
    {
        this.getParentCategory().getSubCategories().remove(this);
        this.setParentCategory(newParent);
        newParent.getSubCategories().add(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProductCategory pc)) return false;
        return id != null && id.equals(pc.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
