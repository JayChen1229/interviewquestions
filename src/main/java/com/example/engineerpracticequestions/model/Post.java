package com.example.engineerpracticequestions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetAllPosts",
                procedureName = "GetAllPosts",
                resultClasses = { Post.class }
        ),
        @NamedStoredProcedureQuery(
                name = "FindPostById",
                procedureName = "FindPostById",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_post_id", type = Long.class)
                },
                resultClasses = { Post.class }
        ),
        @NamedStoredProcedureQuery(
                name = "SaveOrUpdatePost",
                procedureName = "SaveOrUpdatePost",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_post_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_content", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_image", type = byte[].class)
                },
                resultClasses = { Post.class }
        ),
        @NamedStoredProcedureQuery(
                name = "FindPostByCommentId",
                procedureName = "FindPostByCommentId",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_comment_id", type = Long.class)
                },
                resultClasses = { Post.class }
        )
})
@Getter
@Setter
@ToString
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

