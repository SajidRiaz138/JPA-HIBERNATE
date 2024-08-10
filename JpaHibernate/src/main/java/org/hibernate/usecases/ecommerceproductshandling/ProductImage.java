package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "product_image")
@Data
@NoArgsConstructor
public class ProductImage
{
    @Id
    @Column (name = "image_id")
    private Integer id;
    @Column (name = "image_filename", length = 400)
    private String imageFileName;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_item_id")
    private ProductItem productItem;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProductImage pi)) return false;
        return id != null && id.equals(pi.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
