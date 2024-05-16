package com.renegz.pnccontroller.domain.entities;

import com.renegz.pnccontroller.utils.Encrypter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "sec01_users")
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    private String username;
    private String email;
    @Convert(converter = Encrypter.class) private String password;
}
