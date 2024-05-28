package com.renegz.pnccontroller.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sec01_books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID code;
    private String isbn;
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_code")
    private Category category;
    private String author;
}
