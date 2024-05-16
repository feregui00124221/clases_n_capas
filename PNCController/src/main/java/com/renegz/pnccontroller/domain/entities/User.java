package com.renegz.pnccontroller.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "sec01_users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    private String username;
    private String password;
    private String email;
}
