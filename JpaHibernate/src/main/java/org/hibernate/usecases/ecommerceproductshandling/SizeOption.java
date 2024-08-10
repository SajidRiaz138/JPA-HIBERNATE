package org.hibernate.usecases.ecommerceproductshandling;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "size_option")
@Data
@NoArgsConstructor
public class SizeOption
{
    @Id
    @Column (name = "size_id")
    private Integer id;

    @Column (name = "size_name", length = 100)
    private String sizeName;

    @Column (name = "sort_order")
    private Integer sortOrder;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "size_category_id")
    private SizeCategory sizeCategory;

    @OneToMany (mappedBy = "sizeOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ProductVariation> productVariations = new HashSet<>();

    public void addProductVariation(ProductVariation productVariation)
    {
        productVariations.add(productVariation);
        productVariation.setSizeOption(this);
    }

    public void removeProductVariation(ProductVariation productVariation)
    {
        productVariations.remove(productVariation);
        productVariation.setSizeOption(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof SizeOption sizeOption)) return false;
        return id != null && id.equals(sizeOption.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
