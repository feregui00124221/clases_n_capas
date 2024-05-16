package com.renegz.pnccontroller.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "sec01_categories")
public class Category {
    @Id
    private String code;
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore // To avoid infinite recursion
    private List<Book> books;
}
