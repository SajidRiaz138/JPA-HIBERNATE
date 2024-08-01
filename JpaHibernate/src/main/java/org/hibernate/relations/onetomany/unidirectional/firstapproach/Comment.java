package org.hibernate.relations.onetomany.unidirectional.firstapproach;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "comment")
@Table(name = "post_comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment
{
    @Id
    @GeneratedValue
    private Long id;

    private String review;
}
