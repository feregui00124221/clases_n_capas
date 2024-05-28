package com.renegz.pnccontroller.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sec01_bookLoans")
public class BookLoan {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date loanDate;
    private Date returnDate;
    private Date dueDate;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private User user;
}
