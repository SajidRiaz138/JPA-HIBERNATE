package org.hibernate.relations.compositekeymapping;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * USE CASE: if the database does not support SEQUENCE objects and use Identity generation Strategy.
 *
 * need to set up able manually and set identity constraint.
 *
 * CREATE TABLE orders (
 *     orderDate DATE,
 *     totalAmount DOUBLE,
 *     version INTEGER,
 *     customerId BIGINT NOT NULL,
 *     orderNumber BIGINT NOT NULL AUTO_INCREMENT, Or IDENTITY depend on the database
 *     PRIMARY KEY (customerId, orderNumber)
 * );
 */

public class CompositeKeyWithIdentityAutoGeneration
{
    @Entity (name = "Order")
    @Table (name = "orders")
    @SQLInsert (sql = "insert into orders (orderDate, totalAmount, version, customerId) values (?, ?,?,?)")
    @Getter
    @Setter
    public static class Order
    {
        @EmbeddedId
        private OrderId orderId;
        private LocalDate orderDate;
        private Double totalAmount;

        @Version
        @Column (insertable = false)
        private Integer version;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class OrderId implements Serializable
    {
        //@Column(name = "order_number")
        private Integer orderNumber;
        private Long customerId;

        public OrderId(Integer orderNumber, Long customerId)
        {
            this.orderNumber = orderNumber;
            this.customerId = customerId;
        }
    }
}
