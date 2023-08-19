package com.example.engineerpracticequestions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String email;
    private String password; // Hashed password
    private byte[] coverImage;
    private String biography;

    @Transient
    private String imgUrl;

}

