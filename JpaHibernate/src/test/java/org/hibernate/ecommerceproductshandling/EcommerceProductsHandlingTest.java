package org.hibernate.ecommerceproductshandling;

import org.hibernate.common.AbstractTest;
import org.hibernate.usecases.ecommerceproductshandling.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EcommerceProductsHandlingTest extends AbstractTest
{
    @Test
    public void testEcommerceProductHandlingWorkflow()
    {
        transactionManager.executeTransaction(transactionManager ->
        {
            AttributeType attributeType = new AttributeType();
            attributeType.setId(1);
            attributeType.setAttributeName("Sale/New Season");

            transactionManager.persist(attributeType);

            AttributeOption attributeOption = new AttributeOption();
            attributeOption.setId(2);
            attributeOption.setAttributeOptionName("New Season");
            attributeOption.setAttributeType(attributeType);

            transactionManager.persist(attributeOption);

            Brand brand = new Brand();
            brand.setId(1);
            brand.setBrandName("Jack & Jones");
            brand.setBrandDescription(
                    "Founded in the 90s as a jeanswear brand, Danish label Jack & Jones has since gone on to expand its sartorial offering to include everything from jumpers, jackets and T-shirts to shoes, underwear and accessories (alongside more of its flex-worthy denim, of course). Scroll the Jack & Jones at ASOS edit to check out our latest drop of the brand’s laid-back pieces.");

            transactionManager.persist(brand);

            Color color = new Color();
            color.setId(1);
            color.setColorName("Navy Blazer");
            transactionManager.persist(color);

            // Step 1: Create and persist parent category (Men)
            ProductCategory menCategory = new ProductCategory();
            menCategory.setId(13); // Assuming you want to set the ID explicitly
            menCategory.setCategoryName("Men");
            menCategory.setCategoryImage("C/images/men.png");

            transactionManager.persist(menCategory); // Persist the parent category first

            // Step 2: Create and persist child category (Coats + Jackets)
            ProductCategory coatsAndJackets = new ProductCategory();
            coatsAndJackets.setId(7);
            coatsAndJackets.setCategoryName("Coats + Jackets");
            coatsAndJackets.setCategoryImage("/images/coats_men.png");
            coatsAndJackets.setCategoryDescription("Layer up for less with key outerwear...");
            coatsAndJackets.setParentCategory(menCategory);

            menCategory.addSubCategory(coatsAndJackets); // Link child to parent
            transactionManager.persist(coatsAndJackets);

            Product product = new Product();

            product.setId(1);
            product.setName("Jack & Jones parka with faux fur hood in navy");
            product.setDescription(
                    "Your go-to for cooler climes. Fixed hood. Faux-fur trim. Zip and press-stud fastening. Functional pockets. Regular fit");
            product.setBrand(brand);
            product.setModelHeight("183cm / 6 ft 0 in");
            product.setModelWearing("Medium");
            product.setCareInstructions("ium','Machine wash according to instructions on care label','");
            product.setAbout("Plain-woven fabric. Main: 100% Polyester");
            product.setCategory(menCategory);
            product.addAttributeOptionToProduct(product, attributeOption);
            brand.addProduct(product);
            transactionManager.persist(product);

            ProductItem productItem = new ProductItem();
            productItem.setId(1);
            productItem.setProduct(product);
            productItem.setColor(color);
            productItem.setOriginalPrice(6500);
            productItem.setSalePrice(3900);
            productItem.setProductCode(129299186);
            product.addProductItem(productItem);
            transactionManager.persist(productItem);

            ProductImage productImage = new ProductImage();
            productImage.setId(1);
            productImage.setImageFileName("image004.png");
            productImage.setProductItem(productItem);
            transactionManager.persist(productImage);

            SizeCategory sizeCategory = new SizeCategory();
            sizeCategory.setCategoryId(1);
            sizeCategory.setCategoryName("Men Clothing");
            sizeCategory.addProductCategory(menCategory);
            sizeCategory.addProductCategory(coatsAndJackets);
            transactionManager.persist(sizeCategory);

            SizeOption sizeOption = new SizeOption();
            sizeOption.setId(1);
            sizeOption.setSizeName("XS - Chest 92cm");
            sizeOption.setSortOrder(1);
            sizeOption.setSizeCategory(sizeCategory);
            transactionManager.persist(sizeOption);

            ProductVariation productVariation = new ProductVariation();
            productVariation.setId(1);
            productVariation.setProductItem(productItem);
            productVariation.setSizeOption(sizeOption);
            productVariation.setQuantityInStock(4);
            transactionManager.persist(productVariation);
        }, entities);

        transactionManager.executeTransaction(transactionManager ->
        {
            Product product = transactionManager.find(Product.class, 1);
            assertNotNull(product);
            assertEquals(1, product.getProductItems().size());
            assertEquals(1, product.getAttributeOptions().size());
            ProductCategory productCategory = transactionManager.find(ProductCategory.class, 13);
            assertEquals(productCategory, product.getCategory());
            assertNotNull(transactionManager.find(Brand.class, 1));
            assertTrue(transactionManager.find(Brand.class, 1).getProducts().contains(product));
            assertNotNull(transactionManager.find(ProductItem.class, 1));
            assertTrue(product.getProductItems().contains(transactionManager.find(ProductItem.class, 1)));
            assertNotNull(transactionManager.find(ProductVariation.class, 1));
            assertNotNull(transactionManager.find(SizeOption.class, 1));
            assertNotNull(transactionManager.find(Color.class, 1));
        }, entities);
    }

    @Override
    public Class<?>[] getEntities()
    {
        return new Class[] { Brand.class, ProductCategory.class, Product.class, SizeCategory.class,
                             AttributeOption.class, AttributeType.class, ProductItem.class, Color.class, SizeOption.class,
                             ProductImage.class,
                             ProductVariation.class };
    }
}
