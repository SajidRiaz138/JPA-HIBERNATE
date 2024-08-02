package org.hibernate.compositekeymapping;

import static org.hibernate.relations.compositekeymapping.CompositeKeyRelationshipAdvancedMapping.BookId;
import static org.hibernate.relations.compositekeymapping.CompositeKeyRelationshipAdvancedMapping.Book;
import static org.hibernate.relations.compositekeymapping.CompositeKeyRelationshipAdvancedMapping.Library;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Test;

public class CompositeKeyRelationshipAdvancedMappingTest extends AbstractTest
{
    @Test
    public void testLibraryBookMapping()
    {
        transactionManager.executeTransaction(em ->
        {
            // Create and persist Libraries
            Library centralLibrary = new Library();
            centralLibrary.setLibraryId(1L);
            centralLibrary.setLibraryName("Central");
            centralLibrary.setLocation("Downtown");

            Library westsideLibrary = new Library();
            westsideLibrary.setLibraryId(2L);
            westsideLibrary.setLibraryName("Westside");
            westsideLibrary.setLocation("West Avenue");

            em.persist(centralLibrary);
            em.persist(westsideLibrary);

            // Create and persist Books
            Book book1 = new Book("978-1234567890", centralLibrary, "Hibernate in Action", "John Doe");
            Book book2 = new Book("978-1234567890", westsideLibrary, "Hibernate in Action", "John Doe");
            Book book3 = new Book("978-0987654321", centralLibrary, "Spring in Action", "Jane Smith");

            em.persist(book1);
            em.persist(book2);
            em.persist(book3);

        }, entities);

        transactionManager.executeTransaction(entityManager ->
        {
            Library foundLibrary = entityManager.find(Library.class, 1L);
            assertNotNull(foundLibrary);

            Book foundBook = entityManager.find(Book.class, new BookId("978-1234567890", 1L));
            assertNotNull(foundBook);
            assertEquals("Hibernate in Action", foundBook.getTitle());
        }, entities);
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Book.class, Library.class };
    }
}
