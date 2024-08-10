package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "product")
@Data
@NoArgsConstructor
public class Product
{
    @Id
    @Column (name = "product_id")
    private Integer id;

    @ManyToOne
    @JoinColumn (name = "product_category_id", nullable = false)
    private ProductCategory category;

    @ManyToOne
    @JoinColumn (name = "brand_id", nullable = false)
    private Brand brand;

    @Column (name = "product_name", nullable = false, length = 255)
    private String name;

    @Column (name = "product_description", length = 2000)
    private String description;

    @Column (name = "model_height")
    private String modelHeight;

    @Column (name = "model_wearing", length = 100)
    private String modelWearing;

    @Column (name = "care_instructions", length = 1000)
    private String careInstructions;

    @Column (name = "about", length = 2000)
    private String about;

    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProductItem> productItems = new HashSet<>();

    @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable (name = "product_attribute", joinColumns = @JoinColumn (name = "product_id"),
            inverseJoinColumns = @JoinColumn (name = "attribute_option_id"))
    private final Set<AttributeOption> attributeOptions = new HashSet<>();

    public void addAttributeOptionToProduct(Product product, AttributeOption attributeOption)
    {
        product.getAttributeOptions().add(attributeOption);
        attributeOption.getProducts().add(product);
    }

    public void removeAttributeOptionFromProduct(Product product, AttributeOption attributeOption)
    {
        product.getAttributeOptions().remove(attributeOption);
        attributeOption.getProducts().remove(product);
    }

    public void addProductItem(ProductItem productItem)
    {
        productItems.add(productItem);
        productItem.setProduct(this);
    }

    public void removeProductItem(ProductItem productItem)
    {
        productItems.remove(productItem);
        productItem.setProduct(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return id != null && id.equals(((Product) o).getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
