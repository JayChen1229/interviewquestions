package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findCommentByPostId(Post post);

    @Procedure(name = "FindCommentsByPostId")
    List<Comment> findCommentByPostId(@Param("p_post_id") Long postId);

    @Procedure(name = "SaveOrUpdateComment")
    Comment saveOrUpdateComment(
            @Param("p_comment_id") Long commentId,
            @Param("p_user_id") Long userId,
            @Param("p_post_id") Long postId,
            @Param("p_content") String content
    );

}
