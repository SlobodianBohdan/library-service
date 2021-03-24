package org.libraryservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Data
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user")
    private Set<Book> books;
}
