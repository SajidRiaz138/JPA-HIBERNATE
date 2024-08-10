package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "product_item")
@Data
@NoArgsConstructor
public class ProductItem
{
    @Id
    @Column (name = "product_item_id")
    private Integer id;
    @Column (name = "original_price")
    private Integer originalPrice;
    @Column (name = "sale_price")
    private Integer salePrice;
    @Column (name = "product_code", length = 20)
    private String productCode;
    @OneToMany (mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProductVariation> productVariations = new HashSet<>();
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "color_id")
    private Color color;
    @OneToMany (mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProductImage> productImages = new HashSet<>();
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_id")
    private Product product;

    public void addProductVariation(ProductVariation productVariation)
    {
        productVariations.add(productVariation);
        productVariation.setProductItem(this);
    }

    public void removeProductVariation(ProductVariation productVariation)
    {
        productVariations.remove(productVariation);
        productVariation.setProductItem(null);
    }

    public void addProductImage(ProductImage productImage)
    {
        productImages.add(productImage);
        productImage.setProductItem(this);
    }

    public void removeProductImage(ProductImage productImage)
    {
        productImages.remove(productImage);
        productImage.setProductItem(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProductItem pi)) return false;
        return id != null && id.equals(pi.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
