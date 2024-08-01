package org.hibernate.relations.onetomany.unidirectional.firstapproach;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Post")
@Table(name = "post")
@NoArgsConstructor
@Getter
@Setter
public class Post
{
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToMany (
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
}
