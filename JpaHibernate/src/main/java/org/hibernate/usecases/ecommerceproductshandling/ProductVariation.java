package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "product_variation")
@Data
@NoArgsConstructor
public class ProductVariation
{
    @Id
    @Column (name = "variation_id")
    private Integer id;

    @Column (name = "quantity_in_stock")
    private Integer quantityInStock;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "size_id")
    private SizeOption sizeOption;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_item_id")
    private ProductItem productItem;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProductVariation pv)) return false;
        return id != null && id.equals(pv.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
