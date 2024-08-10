# Jakarta Persistence API (JPA) and Hibernate Best Practices

This repository contains examples and best practices for using Jakarta Persistence API (JPA) and Hibernate. The goal is to help developers achieve high performance and efficient data access in their applications.
This repository contains numerous examples demonstrating how to achieve high performance and efficiency using JPA and Hibernate.
## Use case 1 : Database Design: eCommerce Products

## Table of Contents

- [Introduction](#introduction)
- [Best Practices](#best-practices)
    - [Entity Design](#entity-design)
    - [Entities Relationship](#entities-relationship)
    - [Transaction Management](#transaction-management)
    - [Batch Processing](#batch-processing)
    - [Caching](#caching)
    - [Fetching Strategies](#fetching-strategies)
    - [JPQL and Criteria API](#jpql-and-criteria-api)
    - [Schema Generation](#schema-generation)
- [High Performance Tips](#high-performance-tips)
    - [Connection Pooling](#connection-pooling)
    - [Indexing](#indexing)
    - [SQL Optimization](#sql-optimization)
    - [Profiling and Monitoring](#profiling-and-monitoring)

## Introduction

Jakarta Persistence API (JPA) is a specification for accessing, persisting, and managing data between Java objects and a relational database. Hibernate is an implementation of JPA and is one of the most widely used ORM frameworks in Java applications.

## Best Practices

### Entity Design

1. **Use Proper Annotations**: Annotate your entities correctly using `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, etc.
2. **Avoid Complex Inheritance**: Prefer composition over inheritance to keep your entity model simple.
3. **Define Equals and HashCode**: Implement `equals` and `hashCode` methods based on business keys, not primary keys.

### Entities Relationships

1. **Use Proper directional relationship**
2. **OneToMany relationship**: The most effective way to map a @OneToMany association is to rely on the `@ManyToOne` side to propagate all entity state changes.
     Bidirectional `@OneToMany` associations are preferable to unidirectional ones because they leverage the `@ManyToOne`relationship, which is more efficient
     in terms of the generated SQL statements.
3. **OneToOne relationship** : When using JPA and Hibernate, a OneToOne association should always share the primary key 
     with the parent table. Additionally, unless bytecode enhancement is being utilized, it's best to avoid bidirectional associations.
     The optimal way to map a `@OneToOne` relationship is by using `@MapsId`. This approach eliminates the need for a bidirectional association, 
     as you can always retrieve the child entity using the parent entity's identifier.
     The `@MapsId` annotation allows us to get the proper one-to-one table relationship and avoid the N+1 query issue if the parent side uses the optional attribute to the value of false.
4. **ManyToMany relationship**: When using the @ManyToMany annotation, always use a java.util.Set and avoid the java.util.List.
   If maintaining a specific entry order is a concern, consider using a SortedSet instead of a Set, and apply 
   either `@SortNatural` or `@SortComparator`. For `@SortNatural`, entity need to implement Comparable. for example,

    `@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @SortNatural
    private SortedSet<Category> categories = new TreeSet<>();`

5. **ManyToMany relationship when Join table has extra columns** : If you need to store extra columns in the join table, you should map the join table as a separate entity.
   First, we need to map the composite primary key of the join table. To do this, we create an `@Embeddable` class to represent the composite key.
   An `@Embeddable` type must be Serializable and override the equals and hashCode methods based on its primary keys values. Embedded this composite key into Join table as key.
   A good practice to cache one entity for example Student and Course with Join table Enrollment, Course can be cached if Student side is controlling operations.
   An entity with the `@NaturalId` annotation allows fetching by its business key, while the `@Cache` annotation sets the cache concurrency strategy.
   The `@NaturalIdCache` instructs Hibernate to cache the entity identifier linked to the business key.Use `@NaturalId` in hash equal method.
   To ensure proper consistency, use utility methods for adding and removing entities.
   So Two bidirectional `@OneToMany` relations would be mapped for example Student, Course and Enrollment, One Students has many Enrollments and One Course has also many enrollments.
   Each Enrollment has one Student and one course.

    **Tips and important Note**: When mapping the intermediary join table, it is preferable to map only one side as a bidirectional `@OneToMany`
    association. Otherwise, removing the intermediary join entity will trigger a second SELECT statement. for example, in Student, Course case, simply remove the collection of enrollment.
6. **CompositeKey and autogenerated field**: If Composite key contains one of fields autogenerated, use `@IdClass(CompositeKey.class)` for database that supports sequence id generation.
     For Identity type it is little tricky and required more effort. For this need to create table in database and set column as Identity and manually run the query using `@SQLInsert`.
     `@PrePersist and @PostPersist` also can be handy while handling auto generation values.
7. **To represent a non-primary key @ManyToOne association, use the referencedColumnName attribute of the @JoinColumn annotation to specify the column on the parent side that Hibernate should use to establish the relationship**:

### Transaction Management

1. **Use `@Transactional`**: Ensure all data access operations are wrapped in transactions.
2. **Handle Transactions Explicitly**: For complex transactions, handle transaction boundaries explicitly using `EntityManager` methods.

### Batch Processing

1. **Batch Inserts and Updates**: Use Hibernate's batch processing capabilities by configuring `hibernate.jdbc.batch_size`.
2. **Avoid Large Transactions**: Break down large transactions into smaller, manageable batches.

### Caching

1. **First-Level Cache**: Leverage Hibernate's first-level cache, which is enabled by default.
2. **Second-Level Cache**: Configure a second-level cache using providers like EHCache, Infinispan, or Hazelcast.

### Fetching Strategies

1. **Lazy vs Eager Loading**: Prefer lazy loading for associations unless you are sure you need eager loading.
2. **Fetch Joins**: Use JPQL fetch joins to optimize the number of queries executed.

### JPQL and Criteria API

1. **Use Named Queries**: Define named queries for frequently used JPQL queries.
2. **Criteria API**: Use the Criteria API for dynamic queries to avoid string concatenation and potential injection issues.

### Schema Generation

1. **Automatic Schema Generation**: Use `hibernate.hbm2ddl.auto` for development but avoid it in production.
2. **Manual Schema Management**: Use tools like Flyway or Liquibase for schema migrations in production environments.

## High Performance Tips

### Connection Pooling

1. **Configure Connection Pooling**: Use a robust connection pool like HikariCP for managing database connections.
2. **Optimize Pool Settings**: Tune connection pool settings like `maxPoolSize`, `minIdle`, and `connectionTimeout`.

### Indexing

1. **Database Indexes**: Create indexes on frequently queried columns to improve query performance.
2. **Hibernate Annotations**: Use `@Index` annotations or equivalent DDL scripts to define indexes.

### SQL Optimization

1. **Monitor SQL Queries**: Use Hibernate's logging capabilities to monitor and analyze generated SQL queries.
2. **Avoid N+1 Problem**: Use fetch joins or batch fetching to prevent the N+1 select issue.

### Profiling and Monitoring

1. **Profiling Tools**: Use profiling tools like YourKit, VisualVM, or JProfiler to analyze application performance.
2. **Database Monitoring**: Monitor database performance using tools like pgAdmin, MySQL Workbench, or database-specific monitoring solutions.

## Resources

- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/)
- https://vladmihalcea.com/
- https://www.databasestar.com/