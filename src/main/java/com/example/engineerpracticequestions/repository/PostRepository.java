package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Procedure(name = "GetAllPosts")
    List<Post> findAllPosts();

    @Procedure(name = "FindPostById")
    Post findPostById(@Param("p_post_id") Long postId);

    @Procedure(name = "SaveOrUpdatePost")
    Post saveOrUpdatePost(
            @Param("p_post_id") Long postId,
            @Param("p_user_id") Long userId,
            @Param("p_content") String content,
            @Param("p_image") byte[] image
    );
}
