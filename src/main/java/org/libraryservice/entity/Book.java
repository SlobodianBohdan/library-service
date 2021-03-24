package org.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    
    private boolean isFree;

    @ManyToOne
    private User user;
}
