package org.hibernate.relations.compositekeymapping;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * USE CASE:
 * In a library management system, each book can be associated with multiple libraries.
 * The Books table has a composite primary key consisting of isbn and library_id.
 * This composite key uniquely identifies each book within a library. The library_id references a Library entity.
 */

public class CompositeKeyRelationshipAdvancedMapping
{
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class BookId implements Serializable
    {
        @Column (name = "isbn")
        private String isbn;

        @Column (name = "library_id")
        private Long libraryId;
    }

    @Entity (name = "Library")
    @Table (name = "library")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Library implements Serializable
    {
        @Id
        @EqualsAndHashCode.Exclude
        private Long libraryId;
        private String libraryName;
        private String location;
    }

    @Entity (name = "Book")
    @Table (name = "book")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Book
    {
        @EmbeddedId
        private BookId id;

        private String title;
        private String author;

        //The @ManyToOne association directs Hibernate to disregard inserts and updates for
        // this mapping because the library_id is managed by the @EmbeddedId.

        @ManyToOne
        @JoinColumn (name = "library_id", insertable = false, updatable = false)
        private Library library;

        public Book(String isbn, Library library, String title, String author)
        {
            this.id = new BookId(isbn, library.getLibraryId());
            this.library = library;
            this.title = title;
            this.author = author;
        }
    }
}
