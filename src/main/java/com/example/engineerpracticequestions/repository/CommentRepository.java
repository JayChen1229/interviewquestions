package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
