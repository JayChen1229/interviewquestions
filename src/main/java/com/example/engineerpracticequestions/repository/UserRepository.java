package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Procedure(name = "emailExists")
    Integer  existsByEmail(@Param("p_email") String email);

    @Procedure(name = "saveOrUpdateUser")
    void saveOrUpdateUser(
            @Param("p_user_id") Long userId,
            @Param("p_user_name") String userName,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_cover_image") byte[] coverImage,
            @Param("p_biography") String biography
    );

    @Procedure(name = "FindUserByEmail")
    User findByEmail(@Param("p_email") String email);

    @Procedure(name = "FindUserById")
    User findUserById(@Param("p_user_id") Long id);

    @Procedure(name = "UpdateUserCoverImage")
    void updateUserCoverImage(@Param("p_user_id") Long userId, @Param("p_cover_image") byte[] coverImage);

    @Procedure(name = "FindUserByPostId")
    User findUserByPostId(@Param("p_post_id") Long postId);

    @Procedure(name = "FindUserByCommentId")
    User findUserByCommentId(@Param("p_comment_id") Long commentId);

}
