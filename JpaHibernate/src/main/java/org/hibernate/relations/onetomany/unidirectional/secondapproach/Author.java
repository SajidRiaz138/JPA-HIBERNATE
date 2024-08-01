package org.hibernate.relations.onetomany.unidirectional.secondapproach;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Author
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * The @JoinColumn(name = "author_id") annotation specifies that the author_id column in the 
     * Book table will be used as the foreign key to map the relationship.
     */

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn (name = "author_id") // we are mapping the relationship with a join column
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book)
    {
        books.add(book);
    }

    public void removeBook(Book book)
    {
        books.remove(book);
    }
}
