package org.hibernate.relations.efficientmanytomany;

import static org.hibernate.relations.manytomany.EfficientManyToManyRelation.Product;
import static org.hibernate.relations.manytomany.EfficientManyToManyRelation.Category;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.common.AbstractTest;
import org.junit.jupiter.api.Test;

public class EfficientManyToManyRelationTest extends AbstractTest
{
    @Test
    public void testManyToManyMapping()
    {
        // Create categories
        Category electronics = new Category();
        electronics.setName("Electronics");

        Category gadgets = new Category();
        gadgets.setName("Gadgets");

        // Create products
        Product phone = new Product();
        phone.setName("Smartphone");
        phone.setDescription("A high-end smartphone");
        phone.setPrice(699.99);

        phone.addCategory(electronics);
        phone.addCategory(gadgets);

        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setDescription("A powerful laptop");
        laptop.setPrice(999.99);
        laptop.addCategory(electronics);

        Long phoneId = transactionManager.executeTransaction(entityManager ->
        {
            entityManager.persist(phone);
            entityManager.persist(laptop);
            return phone.getId();
        }, entities);

        transactionManager.executeTransaction(entityManager ->
        {
            Product product = entityManager.find(Product.class, phoneId);
            assertNotNull(product);
            assertEquals(2, product.getCategories().size());

            Category retrievedElectronics = entityManager.find(Category.class, electronics.getId());
            Category retrievedGadgets = entityManager.find(Category.class, gadgets.getId());

            assertNotNull(retrievedElectronics);
            assertNotNull(retrievedGadgets);
            assertTrue(product.getCategories().contains(retrievedElectronics));
            assertTrue(product.getCategories().contains(retrievedGadgets));
        }, entities);

        // Test remove category
        transactionManager.executeTransaction(entityManager ->
        {
            Product product = entityManager.find(Product.class, phoneId);
            assertNotNull(product);

            Category retrievedElectronics = entityManager.find(Category.class, electronics.getId());
            product.removeCategory(retrievedElectronics);
            entityManager.persist(product);

            product = entityManager.find(Product.class, phoneId);
            assertEquals(1, product.getCategories().size());
            assertFalse(product.getCategories().contains(retrievedElectronics));
        }, entities);

        // Test add category
        transactionManager.executeTransaction(entityManager ->
        {
            Product product = entityManager.find(Product.class, phoneId);
            assertNotNull(product);

            Category newCategory = new Category();
            newCategory.setName("New Category");
            entityManager.persist(newCategory);

            product.addCategory(newCategory);
            entityManager.persist(product);

            product = entityManager.find(Product.class, phoneId);
            assertEquals(2, product.getCategories().size());
            assertTrue(product.getCategories().contains(newCategory));
        }, entities);
    }

    @Test
    public void testNaturalId()
    {
        // Create a category
        Category bookCategory = new Category();
        bookCategory.setName("Books");

        Long categoryId = transactionManager.executeTransaction(entityManager ->
        {
            entityManager.persist(bookCategory);
            return bookCategory.getId();
        }, entities);

        transactionManager.executeTransaction(entityManager ->
        {
            Category retrievedCategory = entityManager.find(Category.class, categoryId);
            assertNotNull(retrievedCategory);

            // Retrieve by natural ID (name)
            Category naturalIdCategory = entityManager.unwrap(Session.class)
                    .byNaturalId(Category.class)
                    .using("name", "Books")
                    .load();
            assertNotNull(naturalIdCategory);
            assertEquals("Books", naturalIdCategory.getName());
        }, entities);

        // Test uniqueness of natural ID
        assertThrows(PersistenceException.class, () ->
        {
            transactionManager.executeTransaction(entityManager ->
            {
                Category duplicateCategory = new Category();
                duplicateCategory.setName("Books"); // Same natural ID as the previous one
                entityManager.persist(duplicateCategory);
            }, entities);
        });
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Product.class, Category.class };
    }
}
