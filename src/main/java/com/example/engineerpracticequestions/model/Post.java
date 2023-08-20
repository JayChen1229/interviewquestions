package com.example.engineerpracticequestions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert  //這個注解可以讓 Hibernate 在插入新記錄時只生成非空欄位的 SQL 語句，從而避免將 NULL 值插入 application_time 欄位。
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private Long userId;
    private String content;
    private byte[] image;
    private LocalDateTime createdAt;

    @Transient
    private Comment comment;

    @Transient
    private User user;

    // Getters, Setters, and other necessary methods...
}

