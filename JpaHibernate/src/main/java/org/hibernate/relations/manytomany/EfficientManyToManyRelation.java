package org.hibernate.relations.manytomany;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.transactionmanagement.TransactionManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EfficientManyToManyRelation
{
    @Entity (name = "Product")
    @Table (name = "product")
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Product
    {
        @Id
        @GeneratedValue
        private Long id;

        private String name;
        private String description;
        private double price;

        @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @JoinTable (name = "product_category",
                joinColumns = @JoinColumn (name = "product_id"),
                inverseJoinColumns = @JoinColumn (name = "category_id"))
        private Set<Category> categories = new HashSet<>();

        public void addCategory(Category category)
        {
            categories.add(category);
            category.getProducts().add(this);
        }

        public void removeCategory(Category category)
        {
            categories.remove(category);
            category.getProducts().remove(this);
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

    @Entity (name = "Category")
    @Table (name = "category")
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Category
    {
        @Id
        @GeneratedValue
        private Long id;

        @NaturalId
        private String name;

        @ManyToMany (mappedBy = "categories")
        private Set<Product> products = new HashSet<>();

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Category category = (Category) o;
            return Objects.equals(name, category.name);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(name);
        }
    }

    public static Class<?>[] getEntities()
    {
        return new Class<?>[] { Product.class,
                                Category.class };
    }
}
