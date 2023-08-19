package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findCommentByPostId(Post post);

//    List<Comment> findByPostId(Long postId);

    List<Comment> findByPost(Post post);
}
