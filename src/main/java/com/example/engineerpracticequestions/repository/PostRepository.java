package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
