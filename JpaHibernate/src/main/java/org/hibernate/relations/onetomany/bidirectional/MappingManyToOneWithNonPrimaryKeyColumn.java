package org.hibernate.relations.onetomany.bidirectional;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;

/**
 * To represent a non-primary key @ManyToOne association, use the referencedColumnName attribute of the
 * @JoinColumn annotation to specify the column on the parent side that Hibernate should use to establish the relationship.
 *
 */
public class MappingManyToOneWithNonPrimaryKeyColumn
{
    @Entity (name = "Book")
    @Table (name = "book")
    private static class Book implements Serializable
    {
        // Parent side
        @Id
        @GeneratedValue
        private Long id;

        private String title;

        private String author;

        @NaturalId
        private String isbn;
    }

    @Entity (name = "Publication")
    @Table (name = "publication")
    private static class Publication
    {
        // Child side
        @Id
        @GeneratedValue
        private Long id;

        private String publisher;

        @ManyToOne (fetch = FetchType.LAZY)
        @JoinColumn (name = "isbn", referencedColumnName = "isbn")
        private Book book;

        @Column (name = "price_in_cents",
                nullable = false)
        private Integer priceCents;
        private String currency;
    }
}
