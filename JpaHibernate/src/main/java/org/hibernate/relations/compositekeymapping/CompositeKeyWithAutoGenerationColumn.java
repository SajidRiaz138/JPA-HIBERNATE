package org.hibernate.relations.compositekeymapping;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

public class CompositeKeyWithAutoGenerationColumn
{
    @Entity (name = "Order")
    @Table (name = "orders")
    @IdClass (OrderId.class)
    @Getter
    @Setter
    public static class Order
    {
        @Id
        @GeneratedValue
        @Column (name = "order_number")
        private Long orderNumber;

        @Id
        @Column (name = "customer_id")
        private Long customerId;

        private LocalDate orderDate;
        private Double totalAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class OrderId implements Serializable
    {
        private Long orderNumber;
        private Long customerId;

        public OrderId(Long orderNumber, Long customerId)
        {
            this.orderNumber = orderNumber;
            this.customerId = customerId;
        }
    }
}
